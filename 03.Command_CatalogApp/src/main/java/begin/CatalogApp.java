package begin;

import java.util.Iterator;
import java.util.Map;

public class CatalogApp {

	private static final String NEW_WORKSHOP = "NEW_WORKSHOP";
	private static final String ALL_WORKSHOPS = "ALL_WORKSHOPS";
	private static final String ALL_WORKSHOPS_STYLESHEET = "ALL_WORKSHOPS_STYLESHEET";

	private WorkshopManager workshopManager;

	private HandlerResponse executeActionAndGetResponse(String actionName, Map parameters) {
		if (actionName.equals(NEW_WORKSHOP)) {
			String nextWorkshopID = workshopManager.getNextWorkshopID();
			StringBuffer newWorkshopContents = workshopManager.createNewFileFromTemplate(nextWorkshopID,
					workshopManager.getWorkshopDir(), workshopManager.getWorkshopTemplate());
			workshopManager.addWorkshop(newWorkshopContents);
			parameters.put("id", nextWorkshopID);
			executeActionAndGetResponse(ALL_WORKSHOPS, parameters);
		} else if (actionName.equals(ALL_WORKSHOPS)) {
			XMLBuilder allWorkshopsXml = new XMLBuilder("workshops");
			WorkshopRepository repository = workshopManager.getWorkshopRepository();
			Iterator ids = repository.keyIterator();
			while (ids.hasNext()) {
				String id = (String) ids.next();
				Workshop workshop = repository.getWorkshop(id);
				allWorkshopsXml.addBelowParent("workshop");
				allWorkshopsXml.addAttribute("id", workshop.getID());
				allWorkshopsXml.addAttribute("name", workshop.getName());
				allWorkshopsXml.addAttribute("status", workshop.getStatus());
				allWorkshopsXml.addAttribute("duration", workshop.getDurationAsString());
			}
			String formattedXml = getFormattedData(allWorkshopsXml.toString());
			return new HandlerResponse(new StringBuffer(formattedXml), ALL_WORKSHOPS_STYLESHEET);
		}
		// ...many more "else if" statements
		
		return null;
	}

	private String getFormattedData(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
