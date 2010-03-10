package com.yobotics.simulationconstructionset;



public class CameraConfiguration
{
  protected String name;

  protected String trackXVar = "q_x", trackYVar = "q_y", trackZVar = "q_z";
  protected String dollyXVar = "q_x", dollyYVar = "q_y", dollyZVar = "q_z";

  protected String fieldOfViewVar = null;
  protected double fieldOfView = -1.0; //Camera.DEFAULT_FIELD_OF_VIEW;

  protected boolean isMounted = false;
  protected String mountName = "";

  protected double camX,camY,camZ,fixX,fixY,fixZ;
  protected boolean isTracking = true, isTrackingX = true, isTrackingY = true, isTrackingZ = false;
  protected boolean isDolly = false, isDollyX = true, isDollyY = true, isDollyZ = false;
  protected double trackDX = 0.0, trackDY = 0.0, trackDZ = 0.0;
  protected double dollyDX = 2.0, dollyDY = 12.0, dollyDZ = 0.0;

  //protected double clipNear = 0.1, clipFar = 100.0;
  //protected YoVariable track_x_var, track_y_var, track_z_var, dolly_x_var, dolly_y_var, dolly_z_var;

  public CameraConfiguration(String name)
  {
    this.name = name;
  }

  public String getName(){return name;}

  /*public void setClipDistances(double clipNear, double clipFar)
  {
    this.clipNear = clipNear;
    this.clipFar = clipFar;
  }*/

  public void setCameraMount(String mountName)
  {
    this.isMounted = true;
    this.mountName = mountName;
  }

  public String getCameraMountName()
  {
    return mountName;
  }

  public boolean isCameraMounted()
  {
    return isMounted;
  }

  public void setCameraFieldOfViewVar(String fieldOfViewVar)
  {
    this.fieldOfViewVar = fieldOfViewVar;
  }

  public void setCameraFieldOfView(double fieldOfView)
  {
    this.fieldOfView = fieldOfView;
  }


  public void setCameraTracking(boolean track, boolean trackX, boolean trackY, boolean trackZ)
  {
    this.isTracking = track; this.isTrackingX = trackX; this.isTrackingY = trackY; this.isTrackingZ = trackZ;
  }

  public void setCameraDolly(boolean dolly, boolean dollyX, boolean dollyY, boolean dollyZ)
  {
    this.isDolly = dolly; this.isDollyX = dollyX; this.isDollyY = dollyY; this.isDollyZ = dollyZ;
  }

  public void setCameraTrackingVars(String trackXVar, String trackYVar, String trackZVar)
  {
    this.trackXVar = trackXVar; this.trackYVar = trackYVar; this.trackZVar = trackZVar;
  }

  public void setCameraDollyVars(String dollyXVar, String dollyYVar, String dollyZVar)
  {
    this.dollyXVar = dollyXVar; this.dollyYVar = dollyYVar; this.dollyZVar = dollyZVar;
  }

  public void setCameraTrackingOffsets(double trackDX, double trackDY, double trackDZ)
  {
    this.trackDX = trackDX; this.trackDY = trackDY; this.trackDZ = trackDZ;
  }

  public void setCameraDollyOffsets(double dollyDX, double dollyDY, double dollyDZ)
  {
    this.dollyDX = dollyDX; this.dollyDY = dollyDY; this.dollyDZ = dollyDZ;
  }

  public void setCameraFix(double fixX, double fixY, double fixZ)
  {
    this.fixX = fixX; this.fixY = fixY; this.fixZ = fixZ;
  }

  public void setCameraPosition(double camX, double camY, double camZ)
  {
    this.camX = camX; this.camY = camY; this.camZ = camZ;
  }


}
