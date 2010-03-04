package edu.vanderbilt.android.vuparking.test;

import edu.vanderbilt.android.vuparking.Main;
import edu.vanderbilt.android.vuparking.ParkingDBManager;
import edu.vanderbilt.android.vuparking.ParkingLot;
import android.test.ActivityInstrumentationTestCase2;

public class MultiZoneTest extends ActivityInstrumentationTestCase2<Main> 
{

	Main main;
	
	public MultiZoneTest() 
	{
		super("edu.vanderbilt.android.vuparking", Main.class);
	}

	public static final int ZONE1 = 0;
	public static final int ZONE2 = 1;
	public static final int ZONE3 = 2;
	public static final int ZONE4 = 3;
	public static final int MEDICAL = 4;
	public static final int VISITOR = 5;

	protected void setUp() throws Exception 
	{
		super.setUp();
		main = (Main) getActivity();
	}

	protected void tearDown() throws Exception 
	{
		super.tearDown();
	}
	
	public void testZone1()
	{		
	    ParkingDBManager parkingDb = new ParkingDBManager();
		assertEquals(parkingDb.openDB(), true);
		int num = parkingDb.queryParkingZone(ZONE1).size();
		assert(num == 3);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(ZONE1).get(i);
			assert(p.getZone() == ZONE1);
		}
	}
	
	public void testZone2(){
		ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(ZONE2).size();
		assert(num == 3);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(ZONE2).get(i);
			assert(p.getZone() == ZONE2);
		}
	}
	
	public void testZone3(){
		ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(ZONE3).size();
		assert(num == 2);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(ZONE3).get(i);
			assert(p.getZone() == ZONE3);
		}
	}
	
	public void testZone4(){
		ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(ZONE4).size();
		assert(num == 1);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(ZONE4).get(i);
			assert(p.getZone() == ZONE4);
		}
	}
	
	public void testZoneMedical() {
	    ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(MEDICAL).size();
		assert(num == 5);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(MEDICAL).get(i);
			assert(p.getZone() == MEDICAL);
		}
	}
	
	public void testZoneVisitor() {
		ParkingDBManager parkingDb = new ParkingDBManager();
		assert(parkingDb.openDB() == true);
		int num = parkingDb.queryParkingZone(VISITOR).size();
		assert(num == 2);
		for (int i = 0; i < num; i++)
		{
			ParkingLot p = parkingDb.queryParkingZone(VISITOR).get(i);
			assert(p.getZone() == VISITOR);
		}
	}
	
	/*public void testMultiZone() {
		ParkingDBManager parkingDb = new ParkingDBManager(Main.appContext);
		assert(parkingDb.openDB() == true);
		int[] num = {0,0,0,0,0,0};
		num[ZONE1] = parkingDb.queryParkingZone(ZONE1).size();
		num[ZONE2] = parkingDb.queryParkingZone(ZONE2).size();
		num[ZONE3] = parkingDb.queryParkingZone(ZONE3).size();
		num[ZONE4] = parkingDb.queryParkingZone(ZONE4).size();
		num[MEDICAL] = parkingDb.queryParkingZone(MEDICAL).size();
		num[VISITOR] = parkingDb.queryParkingZone(VISITOR).size();
		assert(num[ZONE1]+num[ZONE2]+num[ZONE3]+num[ZONE4]+num[MEDICAL]+num[VISITOR] == 16);
	}*/
}
