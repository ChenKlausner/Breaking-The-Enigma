package servlets;

import battlefield.BattlefieldManager;
import constants.Constants;
import engine.Engine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import myException.*;
import utils.ServletUtils;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "LoadXML", urlPatterns = "/load-XML")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadXmlServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();
        String usernameFromParameter = (String) request.getSession().getAttribute(Constants.USERNAME);

        Engine engine = ServletUtils.getEngine(getServletContext(), usernameFromParameter);

        for (Part part : parts) {
            try {
                engine.loadMachineDataWithInputStream(part.getInputStream(),usernameFromParameter);
                String battlefieldName = engine.getBattlefieldName();

                if (getServletContext().getAttribute(battlefieldName) == null) {
                    getServletContext().setAttribute(battlefieldName, battlefieldName);
                    response.setStatus(200);
                    out.println("File Loaded successfully");
                    BattlefieldManager battlefieldManager = ServletUtils.getBattlefieldManager(getServletContext());
                    battlefieldManager.addBattlefield(usernameFromParameter,engine.getBattlefield());
                }else {
                    response.setStatus(500);
                    out.println("Current battlefield name already exist");
                }

            } catch (JAXBException | FileNotFoundException e) {
                out.println("File with the specified pathname does not exist!");
            } catch (SuffixIsNotXmlException e) {
                out.println(e.getMessage());
            } catch (AlphabetNotEvenLengthException e) {
                out.println(e.getMessage());
            } catch (InvalidRotorCountException e) {
                out.println(e.getMessage());
            } catch (NotEnoughRotorsException e) {
                out.println(e.getMessage());
            } catch (RotorHasDoubleMappingException e) {
                out.println(e.getMessage());
            } catch (IdIsNotUniqueException e) {
                out.println(e.getMessage());
            } catch (IdIsNotOrderInSequenceFromOneException e) {
                out.println(e.getMessage());
            } catch (NotchIsOutOfRangeException e) {
                out.println(e.getMessage());
            } catch (CharMapToItselfException e) {
                out.println(e.getMessage());
            } catch (ReflrctorsIdOrderIsNotSequenceOfRomanException e) {
                out.println(e.getMessage());
            } catch (InvalidNumOfAgentsException e) {
                out.println(e.getMessage());
            }
        }
    }
}

