<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="edu.vanderbilt.parkingserver.PMF" %>
<%@ page import="edu.vanderbilt.parkingserver.ParkingInfo" %>
<%@ page import="javax.jdo.Query" %>

<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>Vanderbilt Parking Service</title>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAtSfYekqIZU7-P2m4dkIRvRSjAjoY8bL6hhCbXsrDLhklyvxB6xRTZ_Q4IO9TJVisSlryX2MVfRJUCg"
      type="text/javascript"></script>
    <script type="text/javascript">
    
    var address;
    var map;
    var IMAGES = [ "yellow", "blue", "green", "red", "pink", "orange" ];
    var ZONES = ["Zone1", "Zone2", "Zone3", "Zone4", "Medical", "Visitor"];
    
    function load() {
      if (GBrowserIsCompatible()) {
        map = new GMap2(document.getElementById("map"));
        var center = new GLatLng(36.142495,-86.806676);
        map.setCenter(center, 15);
        map.setUIToDefault();
        
        function getIcon(zone) {
          var icon = new GIcon(G_DEFAULT_ICON);
          icon.image = "http://maps.google.com/mapfiles/ms/micons/" + IMAGES[zone] + ".png";
          icon.iconAnchor = new GPoint(16, 16);
          icon.infoWindowAnchor = new GPoint(16, 0);
          icon.iconSize = new GSize(32, 32);
          return icon;
        }
              
        function createMarker(point, zone, index)
        {
           var marker = new GMarker(point, {icon: getIcon(zone), title:index});
           GEvent.addListener(marker, "click", function() {
             marker.openInfoWindowHtml('<b>Lot ID: </b>' + index + '<br>' + 
                                       '<b>Zone: </b>' + ZONES[zone]);
            });
           return marker;
        }
        <%
        PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + ParkingInfo.class.getName();
	    List<ParkingInfo> info = (List<ParkingInfo>) pm.newQuery(query).execute();
        if (!info.isEmpty()) {
          for (ParkingInfo p : info) {
        %>
        var latlng = new GLatLng(<%= p.getLatitude()%>, <%= p.getLongtitude()%>);
        var zone = <%= p.getZone()%>;
        var id = <%= p.getID()%>;
        map.addOverlay(createMarker(latlng, zone, id));
        <% }
        }  %>
    }
    }
    </script>
  </head>
  <body onload="load()" >
    <h1>Vanderbilt Parking Service Admin</h1>
    <div id="map" style="width: 500px; height: 300px"></div><BR>
	<FORM ACTION="/vuparkingservice" METHOD="POST">
      ParkingLot ID:
      <INPUT TYPE="TEXT" NAME="lotid"><BR>
      Operation method:
      <SELECT NAME="method">
      <OPTION SELECTED> query
      <OPTION> modify
      </SELECT><BR>
      ParkingLot Available Spot:
      <INPUT TYPE="TEXT" NAME="num_spot"><BR>
      <INPUT TYPE="SUBMIT" VALUE="Submit">
    </FORM>	
    <table>

      <tr>
        <td><a href="vuparkingservice">GET Response to client (Parking Information)</a></td>
      </tr>
    </table>
  </body>
</html>


