package busradar.madison;

import java.util.ArrayList;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;

class QuadTreeUtil
{
		
public static QuadTree
QuadTree_create(ArrayList<Element> points)
{	

	QuadTree t = new QuadTree();
	
	if (points.size() <= maxchild) {
		t.items = (Element[])points.toArray();
		return;
	}
	
	Collections.sort(points, new Comparator<Element>() {
		public int compare(Element a, Element b) { return a.lon - b.lon; }
		public boolean equals(Object o) { return false; }
	});
			
	midx = points.get(points.size()/2+1).lon;
	
	Collections.sort(points, new Comparator<Element>() {
		public int compare(Element a, Element b) { return a.lat - b.lat; }
		public boolean equals(Object o) { return false; }
	});
	
	midy = points.get(points.size()/2+1).lat;
	
	ArrayList<Element> nwl = new ArrayList<Element>();
	ArrayList<Element> nel = new ArrayList<Element>();
	ArrayList<Element> swl = new ArrayList<Element>();
	ArrayList<Element> sel = new ArrayList<Element>();
	
	for (Element p : points) 
	{
		if (p.lat >= t.midy) { // north
			if (p.lon >= t.midx) { // east
				nel.add(p);
			}
			else { //west
				nwl.add(p);
			}
		}
		else { // south
			if (p.lon >= t.midx) { // east
				sel.add(p);
			}
			else { //west
				swl.add(p);
			}
		}
	}
	
	nw = new QuadTree(nwl);
	ne = new QuadTree(nel);
	sw = new QuadTree(swl);
	se = new QuadTree(sel);
}


public static ArrayList<Element>
QuadTree_get(QuadTree t, int xboundmin, int yboundmin, int xboundmax, int yboundmax, int span)
{	
	ArrayList<Element> l = new ArrayList<Element>();
	
	if (t.items != null) // we're a leaf
	{	
		for (Element p : t.items) {
			if (p.lon >= xboundmin && p.lon <= xboundmax &&
						 p.lat >= yboundmin && p.lat <= yboundmax )
				l.add(p);
		}
		
		return l;
	}
	else {
		
		if (yboundmin < t.midy) { // include south
			if (xboundmin < t.midx) { // include west
				l.addAll(sw.get(xboundmin, yboundmin, xboundmax, yboundmax, span));
			}
			if (xboundmax >= t.midx) { // include east
				l.addAll(se.get(xboundmin, yboundmin, xboundmax, yboundmax, span));
			}
		}
		
		if (yboundmax >= t.midy) { // include north
			if (xboundmin < t.midx) { // include west
				l.addAll(nw.get(xboundmin, yboundmin, xboundmax, yboundmax, span));
			}
			if (xboundmax >= t.midx) { // include east
				l.addAll(ne.get(xboundmin, yboundmin, xboundmax, yboundmax, span));
			}
		}
		
		return l;
	}
}
}