/*****************************************************************************
 *   Copyright VUparking for CS279 project                                   *
 *   Developed by Xiaowei Li, Yuan Zhuang                                    *
 *   Licensed under the Apache License, Version 2.0 (the "License");         *
 *   you may not use this file except in compliance with the License.        *
 *   You may obtain a copy of the License at                                 *
 *                                                                           *
 *   http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                           *
 *   Unless required by applicable law or agreed to in writing, software     *
 *   distributed under the License is distributed on an "AS IS" BASIS,       *
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*
 *   See the License for the specific language governing permissions and     *
 *   limitations under the License.                                          *
 ****************************************************************************/

// This file is written for defining parking information data model at server side.

package edu.vanderbilt.parkingserver;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(detachable="true")
public class ParkingInfo {
	
	//@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    //private Key key;
    
    /*public void setKey(Key key) {
        this.key = key;
    }
    
    public Key getKey() {
        return key;
    }*/
	@PrimaryKey
    @Persistent
    private Long ID;  // Primary key for data store.

    @Persistent
    private String Name;
    
    @Persistent
    private String Address;
    
    @Persistent 
    private int Type;    // Either Garage (0) or ParkingLot (1)
    
    @Persistent 
    private int Zone;    // Five zones plus visitor
    
    @Persistent
    private double Coordinate_x, Coordinate_y; 
    
    @Persistent
    private int Capacity;
    
    @Persistent
    private int Available_num;
    
    @Persistent 
    private int Disable_num;
    
    @Persistent
    private double Rate;    

    public Long getID() 
    {
        return ID;
    }

    public String getName() 
    {
        return Name;
    }
    
    public String getAddress() 
    {
    	return Address;
    }
    
    public int getType() 
    {
    	return Type;
    }
    
    public int getZone() 
    {
    	return Zone;
    }
    
    public double getLatitude() 
    {
    	return Coordinate_x;
    }

    public double getLongtitude() 
    {
    	return Coordinate_y;
    }
    
    public int getCapacity() 
    {
    	return Capacity;
    }
    
    public int getAvailable() 
    {
        return Available_num;
    }
    
    public int getDisable() 
    {
    	return Disable_num;
    }
    
    public double getRate() 
    {
    	return Rate;
    }

    public void setID(Long id) 
    {
    	this.ID = id;
    }

    public void setName(String name) 
    {
        this.Name = name;
    }
    
    public void setAddress(String addr) 
    {
        this.Address = addr;
    }
    
    public void setType(int type) 
    {
        this.Type = type;
    }
    
    public void setZone(int zone) 
    {
        this.Zone = zone;
    }
    
    public void setCoordinates(double x, double y) 
    {
        this.Coordinate_x = x;
        this.Coordinate_y = y;
    }
    
    public void setCapacity(int num) 
    {
        this.Capacity = num;
    }

    public void setAvailable(int num) 
    {
    	 this.Available_num = num;
    }
    
    public void setDisable(int num) 
    {
        this.Disable_num = num;
    }
    
    public void setRate(double rate) 
    {
        this.Rate = rate;
    }    

    public ParkingInfo(int id, int type, int zone, String name, String addr, double x, double y, 
    		           int capacity, int num_disable, int num_available, double rate) 
    {
        this.ID = Long.parseLong(Integer.toString(id));
        this.Name = name;
        this.Address = addr;
        this.Type = type;
        this.Zone = zone;
        this.Coordinate_x = x;
        this.Coordinate_y = y;
        this.Capacity = capacity;
        this.Available_num = num_available;
        this.Disable_num = num_disable;
        this.Rate = rate;
    }
}