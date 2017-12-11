package us.ihmc.robotics.math.filters;

import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Test;

import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.referenceFrame.tools.EuclidFrameRandomTools;
import us.ihmc.euclid.tools.EuclidCoreRandomTools;
import us.ihmc.euclid.tools.EuclidCoreTestTools;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.robotics.math.frames.YoFrameQuaternion;
import us.ihmc.robotics.math.frames.YoFrameVector;
import us.ihmc.yoVariables.registry.YoVariableRegistry;

public class YoIMUMahonyFilterTest
{
   @Test(timeout=10000)
   public void testInitialization() throws Exception
   {
      String imuName = "test";
      String namePrefix = "q_est_";
      String nameSuffix = "";
      double updateDT = 1.0e-3;
      YoVariableRegistry registry = new YoVariableRegistry("test");
      Random random = new Random(3454);
      ReferenceFrame sensorFrame = EuclidFrameRandomTools.nextReferenceFrame(random);
      YoFrameQuaternion actualOrientation = new YoFrameQuaternion("q_act_", sensorFrame, registry);

      YoFrameVector inputAngularVelocity = new YoFrameVector("angularVelocity", sensorFrame, registry);
      YoFrameVector inputLinearAcceleration = new YoFrameVector("linearAcceleration", sensorFrame, registry);
      YoFrameVector inputMagneticVector = new YoFrameVector("magneticDirection", sensorFrame, registry);
      YoIMUMahonyFilter mahonyFilter = new YoIMUMahonyFilter(imuName, namePrefix, nameSuffix, updateDT, sensorFrame, registry);
      mahonyFilter.setInputs(inputAngularVelocity, inputLinearAcceleration, inputMagneticVector);
      mahonyFilter.setGains(0.5, 0.0);
      mahonyFilter.getEstimatedOrientation().set(EuclidCoreRandomTools.nextQuaternion(random));

      actualOrientation.set(EuclidCoreRandomTools.nextQuaternion(random));
      Vector3D zUp = new Vector3D(0.0, 0.0, 1.0);
      actualOrientation.getFrameOrientation().inverseTransform(zUp);
      inputLinearAcceleration.set(zUp);
      inputLinearAcceleration.scale(20.0);

      Vector3D xForward = new Vector3D(1.0, 0.0, 0.0);
      actualOrientation.getFrameOrientation().inverseTransform(xForward);
      inputMagneticVector.set(xForward);
      inputMagneticVector.scale(0.5);

      mahonyFilter.update();
      double currentError = actualOrientation.getFrameOrientation().distance(mahonyFilter.getEstimatedOrientation().getFrameOrientation());

      assertTrue(currentError <= 1.0e-10);
   }

   @Test(timeout=10000)
   public void testConvergenceToStaticOrientation() throws Exception
   {
      String imuName = "test";
      String namePrefix = "q_est_";
      String nameSuffix = "";
      double updateDT = 1.0e-3;
      YoVariableRegistry registry = new YoVariableRegistry("test");
      Random random = new Random(3454);
      ReferenceFrame sensorFrame = EuclidFrameRandomTools.nextReferenceFrame(random);
      YoFrameQuaternion actualOrientation = new YoFrameQuaternion("q_act_", sensorFrame, registry);

      YoFrameVector inputAngularVelocity = new YoFrameVector("angularVelocity", sensorFrame, registry);
      YoFrameVector inputLinearAcceleration = new YoFrameVector("linearAcceleration", sensorFrame, registry);
      YoFrameVector inputMagneticVector = new YoFrameVector("magneticDirection", sensorFrame, registry);
      YoIMUMahonyFilter mahonyFilter = new YoIMUMahonyFilter(imuName, namePrefix, nameSuffix, updateDT, sensorFrame, registry);
      mahonyFilter.setInputs(inputAngularVelocity, inputLinearAcceleration, inputMagneticVector);
      mahonyFilter.setHasBeenInitialized(true); // So the estimated orientation does not get snapped to the actual

      mahonyFilter.setGains(0.5, 0.0);

      actualOrientation.set(EuclidCoreRandomTools.nextQuaternion(random));
      Vector3D zUp = new Vector3D(0.0, 0.0, 1.0);
      actualOrientation.getFrameOrientation().inverseTransform(zUp);
      inputLinearAcceleration.set(zUp);
      inputLinearAcceleration.scale(20.0);

      Vector3D xForward = new Vector3D(1.0, 0.0, 0.0);
      actualOrientation.getFrameOrientation().inverseTransform(xForward);
      inputMagneticVector.set(xForward);
      inputMagneticVector.scale(0.5);

      double currentError = 0.0;
      double previousError = Double.POSITIVE_INFINITY;

      for (int i = 0; i < 50000; i++)
      {
         mahonyFilter.update();

         currentError = actualOrientation.getFrameOrientation().distance(mahonyFilter.getEstimatedOrientation().getFrameOrientation());
         assertTrue(currentError <= previousError);
         previousError = currentError;
      }

      assertTrue(currentError <= 3.0e-4);
   }

   @Test(timeout=10000)
   public void testConvergenceToStaticOrientationWithGyroBias() throws Exception
   {
      String imuName = "test";
      String namePrefix = "q_est_";
      String nameSuffix = "";
      double updateDT = 1.0e-3;
      YoVariableRegistry registry = new YoVariableRegistry("test");
      Random random = new Random(3454);
      ReferenceFrame sensorFrame = EuclidFrameRandomTools.nextReferenceFrame(random);
      YoFrameQuaternion actualOrientation = new YoFrameQuaternion("q_act_", sensorFrame, registry);

      YoFrameVector inputAngularVelocity = new YoFrameVector("angularVelocity", sensorFrame, registry);
      YoFrameVector inputLinearAcceleration = new YoFrameVector("linearAcceleration", sensorFrame, registry);
      YoFrameVector inputMagneticVector = new YoFrameVector("magneticDirection", sensorFrame, registry);
      YoIMUMahonyFilter mahonyFilter = new YoIMUMahonyFilter(imuName, namePrefix, nameSuffix, updateDT, sensorFrame, registry);
      mahonyFilter.setInputs(inputAngularVelocity, inputLinearAcceleration, inputMagneticVector);
      mahonyFilter.setHasBeenInitialized(true); // So the estimated orientation does not get snapped to the actual
      mahonyFilter.setGains(0.5, 0.05);

      inputAngularVelocity.set(EuclidCoreRandomTools.nextVector3DWithFixedLength(random, 0.1));

      actualOrientation.set(EuclidCoreRandomTools.nextQuaternion(random));
      Vector3D zUp = new Vector3D(0.0, 0.0, 1.0);
      actualOrientation.getFrameOrientation().inverseTransform(zUp);
      inputLinearAcceleration.set(zUp);
      inputLinearAcceleration.scale(20.0);

      Vector3D xForward = new Vector3D(1.0, 0.0, 0.0);
      actualOrientation.getFrameOrientation().inverseTransform(xForward);
      inputMagneticVector.set(xForward);
      inputMagneticVector.scale(0.5);

      double currentError = 0.0;

      for (int i = 0; i < 100000; i++)
      {
         mahonyFilter.update();
         currentError = actualOrientation.getFrameOrientation().distance(mahonyFilter.getEstimatedOrientation().getFrameOrientation());
      }

      System.out.println(currentError);
      assertTrue(currentError <= 3.0e-4);
      Vector3D estimatedBias = new Vector3D();
      mahonyFilter.getIntegralTerm().get(estimatedBias);
      estimatedBias.negate();
      EuclidCoreTestTools.assertVector3DGeometricallyEquals(inputAngularVelocity.getFrameTuple(), estimatedBias, 1.0e-4);
   }
}
