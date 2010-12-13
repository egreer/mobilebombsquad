package layar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.jdo.Extent;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import com.google.appengine.api.datastore.Blob;


public class DatabaseAccess {

	/** Stores a POI into the Database
	 * 
	 * @param point
	 * @return
	 */
	public static boolean storePOI(POI point){
		boolean returner = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try	{
			tx.begin();

			pm.makePersistent(point);

			tx.commit();
		}
		finally	{
			if (tx.isActive()){
				tx.rollback();
				returner = false;
			}
		}

		pm.close();

		return returner;
	}
	
	/** Returns a list of POI that are in range
	 * 
	 * @param lat		Current Latitude 
	 * @param lon		Current Longitude
	 * @param radius	Radius to search within
	 * @param currentAlt 
	 * @return
	 */
	public static ArrayList<POI> getPoints(double lat, double lon, double radius, int alt){
		ArrayList<POI> returner = new ArrayList<POI>();
		
		Collection<POI> pois = fetchAllPOIs();
	
		Iterator<POI> iterator = pois.iterator();
		while(iterator.hasNext()){
			POI point = iterator.next();
			point.setDistance(point.calcDistance(lat, lon));
			if(point.getDistance() <= radius){
				if (point.getAlt() != Integer.MIN_VALUE) point.setRelativeAlt(point.getAlt() - alt);
				else point.setRelativeAlt(0);
				returner.add(point);
			}
		}
		
		return returner;
	}
	
	/**
	 * 
	 * @return Returns all points in the database
	 */
	public static Collection<POI> fetchAllPOIs() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<POI> returner = new ArrayList<POI>();
		Extent<POI> pois = pm.getExtent(POI.class);

		Iterator<POI> iterator = pois.iterator();
		while (iterator.hasNext()){
			returner.add(iterator.next());
		}
		pm.close();
		return (Collection<POI>) returner;
	}

	
	public static Blob getImage(String name) {
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	         
	    Extent<MyImage> results =  pm.getExtent(MyImage.class);
	    Iterator<MyImage> iterator = results.iterator();
	    
	    while(iterator.hasNext()){
	    	MyImage image = iterator.next();
	    	if(image.getName().equals(name)){
	    		return image.getImage();
	    	}
	    }
	    
		return null;
	}
	
	
	public static boolean storeImage(MyImage image) {
		boolean returner = true;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Transaction tx = pm.currentTransaction();
		try	{
			tx.begin();

			pm.makePersistent(image);

			tx.commit();
		}
		finally	{
			if (tx.isActive()){
				tx.rollback();
				returner = false;
			}
		}

		pm.close();

		return returner;
	}
}
