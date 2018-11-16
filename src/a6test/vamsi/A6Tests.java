package a6test.vamsi;

import static org.junit.Assert.*;

import org.junit.Test;

import a6.*;

public class A6Tests {
	
	// Initialize different pixel amounts.
	Pixel red = new ColorPixel(1, 0, 0);
	Pixel green = new ColorPixel(0, 1, 0);
	Pixel blue = new ColorPixel(0, 0, 1);
	
	@Test
	public void testCreationOfRegion() {
		Region newRegion = new RegionImpl(1,1, 10, 11);
		assertEquals(1, newRegion.getLeft());
		assertEquals(1, newRegion.getTop());
		assertEquals(10, newRegion.getRight());
		assertEquals(11, newRegion.getBottom());
	}
	
	@Test
	public void testRegionIntersect() {
		Region a = new RegionImpl(1, 1, 5, 6);
		Region b = new RegionImpl(3, 4, 11, 10);
		
		try {
			Region iRegion = a.intersect(b);
			assertEquals(3, iRegion.getLeft());
			assertEquals(4, iRegion.getTop());
			assertEquals(5, iRegion.getRight());
			assertEquals(6, iRegion.getBottom());
		} catch (NoIntersectionException e) {
		}
	}
	
	@Test
	public void testReverseRegionIntersect() {
		Region a = new RegionImpl(1, 1, 5, 6);
		Region b = new RegionImpl(3, 4, 11, 10);
		
		try {
			Region iRegion = b.intersect(a);
			assertEquals(3, iRegion.getLeft());
			assertEquals(4, iRegion.getTop());
			assertEquals(5, iRegion.getRight());
			assertEquals(6, iRegion.getBottom());
		} catch (NoIntersectionException e) {
		}
	}

	@Test
	public void testIntersectException() {
		Region a = new RegionImpl(1, 1, 5, 6);
		Region b = new RegionImpl(10, 10, 12, 15);
		
		try {
			Region iRegion = b.intersect(a);
			fail("No intersection");
		} catch (NoIntersectionException e) {
		}
	}
	
	@Test
	public void testRegionUnion() {
		Region a = new RegionImpl(1, 1, 5, 6);
		Region b = new RegionImpl(3, 4, 11, 10);
		
		Region uRegion = b.union(a);
		assertEquals(1, uRegion.getLeft());
		assertEquals(1, uRegion.getTop());
		assertEquals(11, uRegion.getRight());
		assertEquals(10, uRegion.getBottom());
	}
	
	@Test
	public void testReverseRegionUnion() {
		Region a = new RegionImpl(1, 1, 5, 6);
		Region b = new RegionImpl(3, 4, 11, 10);
		
		Region uRegion = a.union(b);
		assertEquals(1, uRegion.getLeft());
		assertEquals(1, uRegion.getTop());
		assertEquals(11, uRegion.getRight());
		assertEquals(10, uRegion.getBottom());
	}
	
	@Test
	public void testObservablePictureImpl() {
		Pixel[][] parray = new Pixel[5][10];
		
		for (int x=0; x<5; x++) {
			for (int y=0; y<10; y++) {
				parray[x][y] = red;
			}
		}
		
		Picture p = new MutablePixelArrayPicture(parray, "Picture");
		
		ObservablePicture op = new ObservablePictureImpl(p);
		assertEquals("Picture", op.getCaption());
		assertEquals(5, op.getWidth());
		assertEquals(10, op.getHeight());
	}
	
	@Test
	public void testRegisterWithObservablePicture() {
		Pixel[][] parray = new Pixel[5][10];
		
		for (int x=0; x<5; x++) {
			for (int y=0; y<10; y++) {
				parray[x][y] = red;
			}
		}
		
		Picture p = new MutablePixelArrayPicture(parray, "Picture");
		ObservablePicture op = new ObservablePictureImpl(p);
		Region subscribedRegion = new RegionImpl(1, 1, 5, 6);
		
		ROIObserver o = new ROIObserverImpl(op, subscribedRegion);
		
		ROIObserver[] observerList = op.findROIObservers(subscribedRegion);
		
		assertEquals(1, observerList.length);
	}
	
	@Test
	public void testFindROIObserversPositive() {
		Pixel[][] parray = new Pixel[5][10];
		
		for (int x=0; x<5; x++) {
			for (int y=0; y<10; y++) {
				parray[x][y] = red;
			}
		}
		
		Picture p = new MutablePixelArrayPicture(parray, "Picture");
		ObservablePicture op = new ObservablePictureImpl(p);
		
		Region subscribedRegion = new RegionImpl(1, 1, 5, 6);
		
		ROIObserver o1 = new ROIObserverImpl(op, subscribedRegion);
		ROIObserver o2 = new ROIObserverImpl(op, subscribedRegion);
		
		Region changedRegion = new RegionImpl(2, 2, 3, 3);
		
		ROIObserver[] observerList = op.findROIObservers(changedRegion);
		assertEquals(2, observerList.length);
	}
	
	@Test
	public void testFindROIObserversNegative() {
		Pixel[][] parray = new Pixel[5][10];
		
		for (int x=0; x<5; x++) {
			for (int y=0; y<10; y++) {
				parray[x][y] = red;
			}
		}
		
		Picture p = new MutablePixelArrayPicture(parray, "Picture");
		ObservablePicture op = new ObservablePictureImpl(p);
		
		Region subscribedRegion = new RegionImpl(1, 1, 5, 6);
		
		ROIObserver o1 = new ROIObserverImpl(op, subscribedRegion);
		ROIObserver o2 = new ROIObserverImpl(op, subscribedRegion);
		
		Region changedRegion = new RegionImpl(0, 0, 0, 0);
		ROIObserver[] observerList = op.findROIObservers(changedRegion);
		
		assertEquals(0, observerList.length);
	}
	
	@Test
	public void testPixelPaintNotify() {
		Pixel[][] parray = new Pixel[5][10];
		
		for (int x=0; x<5; x++) {
			for (int y=0; y<10; y++) {
				parray[x][y] = red;
			}
		}
		
		Picture p = new MutablePixelArrayPicture(parray, "Picture");
		ObservablePicture op = new ObservablePictureImpl(p);
		
		Region subscribedRegion = new RegionImpl(1, 1, 5, 6);
		ROIObserverImpl o1 = new ROIObserverImpl(op, subscribedRegion);
		
		op.paint(1, 1, blue);
		
		Region changedRegion = o1.getChangedRegion();
		boolean regionsEqual = regionEquals(changedRegion, new RegionImpl(1,1,1,1));
		
		assertEquals(true, regionsEqual);
	}
	
	@Test
	public void testPixelPaintNotifySuspend() {
		Pixel[][] parray = new Pixel[5][10];
		
		for (int x=0; x<5; x++) {
			for (int y=0; y<10; y++) {
				parray[x][y] = red;
			}
		}
		
		Picture p = new MutablePixelArrayPicture(parray, "Picture");
		ObservablePicture op = new ObservablePictureImpl(p);
		
		Region subscribedRegion = new RegionImpl(1, 1, 5, 6);
		ROIObserverImpl o1 = new ROIObserverImpl(op, subscribedRegion);
		
		op.suspendObservable();
		
		op.paint(1, 1, blue);
		
		Region changedRegion = o1.getChangedRegion();
		boolean regionsEqual = regionEquals(changedRegion, new RegionImpl(1,1,1,1));
		
		assertEquals(false, regionsEqual);
	}
	
	@Test
	public void testPixelPaintNotifyResume() {
		Pixel[][] parray = new Pixel[5][10];
		
		for (int x=0; x<5; x++) {
			for (int y=0; y<10; y++) {
				parray[x][y] = red;
			}
		}
		
		Picture p = new MutablePixelArrayPicture(parray, "Picture");
		ObservablePicture op = new ObservablePictureImpl(p);
		
		Region subscribedRegion = new RegionImpl(1, 1, 5, 6);
		ROIObserverImpl o1 = new ROIObserverImpl(op, subscribedRegion);
		
		op.suspendObservable();
		
		op.paint(1, 1, blue);
		op.paint(2, 2, blue);
		
		op.resumeObservable();
		
		Region changedRegion = o1.getChangedRegion();
		boolean regionsEqual = regionEquals(changedRegion, new RegionImpl(1,1,2,2));
		
		assertEquals(true, regionsEqual);
	}
	
	@Test
	public void testUnregisterObserver() {
		Pixel[][] parray = new Pixel[5][10];
		
		for (int x=0; x<5; x++) {
			for (int y=0; y<10; y++) {
				parray[x][y] = red;
			}
		}
		
		Picture p = new MutablePixelArrayPicture(parray, "Picture");
		ObservablePicture op = new ObservablePictureImpl(p);
		
		Region subscribedRegion1 = new RegionImpl(1, 1, 5, 6);
		Region subscribedRegion2 = new RegionImpl(1, 1, 2, 2);
		
		ROIObserverImpl o1 = new ROIObserverImpl(op, subscribedRegion1);
		ROIObserverImpl o2 = new ROIObserverImpl(op, subscribedRegion2);
		
		op.unregisterROIObserver(o1);
		
		op.paint(2, 2, blue);
		
		Region changedRegion = o1.getChangedRegion();
		boolean regionsEqual = regionEquals(changedRegion, new RegionImpl(2,2,2,2));
		assertEquals(false, regionsEqual);
		
		changedRegion = o2.getChangedRegion();
		regionsEqual = regionEquals(changedRegion, new RegionImpl(2,2,2,2));
		assertEquals(true, regionsEqual);
	}
	
	@Test
	public void testCirclePaint() {
Pixel[][] parray = new Pixel[5][10];
		
		for (int x=0; x<5; x++) {
			for (int y=0; y<10; y++) {
				parray[x][y] = red;
			}
		}
		
		Picture p = new MutablePixelArrayPicture(parray, "Picture");
		ObservablePicture op = new ObservablePictureImpl(p);
		
		Region subscribedRegion = new RegionImpl(1, 1, 5, 6);
		ROIObserverImpl o1 = new ROIObserverImpl(op, subscribedRegion);
		
		op.paint(2, 3, 1, blue);
		
		Region changedRegion = o1.getChangedRegion();
		boolean regionsEqual = regionEquals(changedRegion, new RegionImpl(1,2,3,4));
		assertEquals(true, regionsEqual);
	}
	
	// helper method
	public boolean regionEquals(Region r1, Region r2) {
			if (r1 == null || r2 == null)
				return false;
			
			if (r1.getLeft() == r2.getLeft() && 
					r1.getTop() == r2.getTop() && 
					r1.getRight() == r2.getRight() &&
					r1.getBottom() == r2.getBottom()) {
				return true;
			}
			
			return false;
	}

}
