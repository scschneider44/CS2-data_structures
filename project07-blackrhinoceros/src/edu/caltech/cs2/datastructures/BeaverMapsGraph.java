package edu.caltech.cs2.datastructures;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import edu.caltech.cs2.interfaces.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class BeaverMapsGraph extends Graph<Long, Double> {
    private static JsonParser JSON_PARSER = new JsonParser();

    private IDictionary<Long, Location> ids;
    private ISet<Location> buildings;
   // private IGraph<Long, Double> graph;

    public BeaverMapsGraph() {
        super();
        this.buildings = new ChainingHashSet<>();
        this.ids = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    /**
     * Reads in buildings, waypoinnts, and roads file into this graph.
     * Populates the ids, buildings, vertices, and edges of the graph
     * @param buildingsFileName the buildings filename
     * @param waypointsFileName the waypoints filename
     * @param roadsFileName the roads filename
     */
    public BeaverMapsGraph(String buildingsFileName, String waypointsFileName, String roadsFileName) {
        this();

        JsonElement bs = fromFile(buildingsFileName);
        for (JsonElement b : bs.getAsJsonArray()) {
            Location loc = new Location(b.getAsJsonObject());
            this.ids.put(loc.id, loc);
            this.buildings.add(loc);
            super.addVertex(loc.id);
            }

        JsonElement ws = fromFile(waypointsFileName);
        for (JsonElement w : ws.getAsJsonArray()) {
            Location waypt = new Location(w.getAsJsonObject());
            this.ids.put(waypt.id, waypt);
            super.addVertex(waypt.id);
        }

        JsonElement rs = fromFile(roadsFileName);
        for (JsonElement r : rs.getAsJsonArray()) {
            JsonArray road = r.getAsJsonArray();
            for (int i=0; i < road.size() - 1; i++) {
                if (i > 0) {
                    double dist2 = ids.get(road.get(i).getAsLong()).getDistance(ids.get(road.get(i-1).getAsLong()));
                    super.addUndirectedEdge(road.get(i).getAsLong(), road.get(i - 1).getAsLong(), dist2);
                }
                double dist1 = ids.get(road.get(i).getAsLong()).getDistance(ids.get(road.get(i+1).getAsLong()));
                super.addUndirectedEdge(road.get(i).getAsLong(), road.get(i + 1).getAsLong(), dist1);

            }
        }
    }

    /**
     * Returns a deque of all the locations with the name locName.
     * @param locName the name of the locations to return
     * @return a deque of all location with the name locName
     */
    public IDeque<Location> getLocationByName(String locName) {
        IDeque<Location> locations = new LinkedDeque<>();
        for (Location building : this.buildings) {
            if (building.name != null && building.name.equals(locName)) {
                locations.addBack(building);
            }
        }
        return locations;
    }

    /**
     * Returns the Location object corresponding to the provided id
     * @param id the id of the object to return
     * @return the location identified by id
     */
    public Location getLocationByID(long id) {
        return this.ids.get(id);
    }

    /**
     * Adds the provided location to this map.
     * @param n the location to add
     * @return true if n is a new location and false otherwise
     */
    public boolean addVertex(Location n) {
        if (!ids.containsKey(n.id)) {
            ids.put(n.id, n);
            super.addVertex(n.id);
            return true;
        }
        return false;
    }

    /**
     * Returns the closest building to the location (lat, lon)
     * @param lat the latitude of the location to search near
     * @param lon the longitute of the location to search near
     * @return the building closest to (lat, lon)
     */
    public Location getClosestBuilding(double lat, double lon) {
        double closest = 1000000;
        Location closeBuilding = null;
        for (Location building : this.buildings) {
            double dist = Math.pow(building.lat - lat, 2) + Math.pow(building.lon - lon, 2);
            if (dist < closest) {
                closest = dist;
                closeBuilding = building;
            }
        }
        return closeBuilding;
    }

    /**
     * Returns a set of locations which are no more than threshold feet
     * away from start.
     * @param start the location to search around
     * @param threshold the number of feet in the search radius
     * @return
     */
    public ISet<Location> dfs(Location start, double threshold) {
        ISet<Location> near = new ChainingHashSet<>();
        dfs(start, start, threshold, near);
        return near;
    }
    private void dfs(Location start, Location newStart, double threshold, ISet<Location> near) {
        ISet<Long> neighbors = super.neighbors(newStart.id);
        near.add(newStart);
        for (Long neighbor : neighbors) {
            double newThresh = threshold - start.getDistance(this.ids.get(neighbor));
            if (this.ids.get(neighbor)!= null && !near.contains(this.ids.get(neighbor)) && newThresh >= 0) {
                dfs(start, this.ids.get(neighbor), threshold, near);
            }
        }
    }

    /**
     * Returns a list of Locations corresponding to
     * buildings in the current map.
     * @return a list of all building locations
     */
    public ISet<Location> getBuildings() {
        return this.buildings;
    }

    /**
     * Returns a shortest path (i.e., a deque of vertices) between the start
     * and target locations (including the start and target locations).
     * @param start the location to start the path from
     * @param target the location to end the path at
     * @return a shortest path between start and target
     */
    public IDeque<Location> dijkstra(Location start, Location target) {

        IPriorityQueue<Location> seen = new MinFourHeap<>();
        IDictionary<Location, Double> priorities = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        IDictionary<Location, Location> parents = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        IDictionary<Location, Double> completed = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        IDeque<Location> path = new LinkedDeque<>();

        IPriorityQueue.PQElement pqStart = new IPriorityQueue.PQElement(start, 0);
        seen.enqueue(pqStart);
        parents.put(start, null);

        while (!completed.containsKey(target)) {

            if (seen.size() == 0) {
                return null;
            }
            IPriorityQueue.PQElement<Location> removed = seen.dequeue();
            completed.put(removed.data, removed.priority);
            ISet<Long> children = super.neighbors(removed.data.id);

            for (Long child : children) {
                if (!completed.containsKey(ids.get(child))){//&& !buildings.contains(ids.get(child))) {
                    Double dist = completed.get(removed.data) + super.adjacent((removed.data).id, child);
                    IPriorityQueue.PQElement pqChild = new IPriorityQueue.PQElement(ids.get(child), dist);
                    if (!this.buildings.contains(ids.get(child)) || ids.get(child).equals(target)) {
                        if (priorities.containsKey(ids.get(child))) {
                            if (dist < priorities.get(ids.get(child))) {
                                seen.decreaseKey(pqChild);
                                parents.put(ids.get(child), removed.data);
                                priorities.put(ids.get(child), dist);
                            }
                        } else {
                            seen.enqueue(pqChild);
                            priorities.put(ids.get(child), dist);
                            parents.put(ids.get(child), removed.data);
                        }
                    }
                }

            }

        }
        Location parent = target;
        path.addBack(target);
        while (!parent.equals(start)) {
            //System.out.println(parent.id);
            parent = parents.get(parent);
            path.addFront(parent);
        }
        /*for (Location thing : parents.keySet() ) {
            if (parents.get(thing) != null) {
                System.out.println(thing.id + " " + parents.get(thing).id);
            }
        }*/
        for (Location thing : path) {
            System.out.print(thing.id + " ");
        }
        return path;
    }

    /**
     * Returns a JsonElement corresponding to the data in the file
     * with the filename filename
     * @param filename the name of the file to return the data from
     * @return the JSON data from filename
     */
    private static JsonElement fromFile(String filename) {
        try {
            return JSON_PARSER.parse(
                    new FileReader(
                            new File(filename)
                    )
            );
        } catch (IOException e) {
            return null;
        }
    }
}