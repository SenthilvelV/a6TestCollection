package a6test.vamsi;

import a6.ObservablePicture;
import a6.ROIObserver;
import a6.Region;

public class ROIObserverImpl implements ROIObserver {
	
	ObservablePicture _oPicture;
	Region _subscribedRegion;
	Region _changedRegion;
	
	public void ROIObserverImpl() {
		
	}
	
	public ROIObserverImpl(ObservablePicture picture, Region subscribedRegion) {
		this._oPicture = picture;
		this._subscribedRegion = subscribedRegion;
		
		_oPicture.registerROIObserver(this, _subscribedRegion);
	}

	@Override
	public void notify(ObservablePicture picture, Region changed_region) {
		this._changedRegion = changed_region;
	}
	
	public Region getChangedRegion() {
		return _changedRegion;
	}

}
