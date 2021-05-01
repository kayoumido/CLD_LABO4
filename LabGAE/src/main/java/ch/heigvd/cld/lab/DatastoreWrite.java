package ch.heigvd.cld.lab;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

@WebServlet(name = "DatastoreWrite", value = "/datastorewrite")
public class DatastoreWrite extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/plain");
        PrintWriter pw = resp.getWriter();

        if(!req.getParameterMap().containsKey("_kind")) {
            pw.println("Property \"_kind\" is mandatory");
            return;
        }

        pw.println("Writing entity to datastore.");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity entity = req.getParameterMap().containsKey("_key") ?
                new Entity(req.getParameter("_kind"), req.getParameter("_key"))
                : new Entity(req.getParameter("_kind"));

        Enumeration<String> enumeration = req.getParameterNames();

        // Retrieve all properties
        while(enumeration.hasMoreElements()) {
            String property = enumeration.nextElement();
            if(!property.equals("_key") && !property.equals("_kind")) {
                entity.setProperty(property, req.getParameter(property));
            }
        }

        datastore.put(entity);
    }
}