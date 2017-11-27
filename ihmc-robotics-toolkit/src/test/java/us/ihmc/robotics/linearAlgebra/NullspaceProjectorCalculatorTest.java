package us.ihmc.robotics.linearAlgebra;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.junit.Test;
import us.ihmc.continuousIntegration.ContinuousIntegrationAnnotations.ContinuousIntegrationTest;

import static org.junit.Assert.assertEquals;

public abstract class NullspaceProjectorCalculatorTest
{
   public abstract NullspaceProjectorCalculator getNullspaceProjectorCalculator();

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 30000)
   public void testSimpleNullspaceProjector()
   {
      NullspaceProjectorCalculator nullspaceProjectorCalculator = getNullspaceProjectorCalculator();

      DenseMatrix64F jacobian = new DenseMatrix64F(2, 2);
      jacobian.set(0, 0, 1.0);
      jacobian.set(0, 1, 3.0);
      jacobian.set(1, 0, 7.0);
      jacobian.set(1, 1, 9.0);

      DenseMatrix64F nullspaceProjector = new DenseMatrix64F(2, 2);
      /*
      nullspaceProjectorCalculator.computeNullspaceProjector(jacobian, nullspaceProjector);

      for (int i = 0; i < nullspaceProjector.getNumElements(); i++)
         assertEquals(nullspaceProjector.get(i), 0.0, 1e-7);
         */


      jacobian = new DenseMatrix64F(2, 4);
      jacobian.set(0, 0, 1.0);
      jacobian.set(0, 1, 3.0);
      jacobian.set(0, 3, 7.0);
      jacobian.set(1, 0, 7.0);
      jacobian.set(1, 2, 9.0);
      jacobian.set(1, 3, 11.0);

      nullspaceProjector = new DenseMatrix64F(4, 4);
      nullspaceProjectorCalculator.computeNullspaceProjector(jacobian, nullspaceProjector);

      DenseMatrix64F nullspaceProjectorExpected = new DenseMatrix64F(4, 4);

      nullspaceProjectorExpected.set(0, 0, 0.746421);
      nullspaceProjectorExpected.set(0, 1, 0.130401);
      nullspaceProjectorExpected.set(0, 2,-0.381917);
      nullspaceProjectorExpected.set(0, 3,-0.162518);

      nullspaceProjectorExpected.set(1, 0, 0.130401);
      nullspaceProjectorExpected.set(1, 1, 0.708629);
      nullspaceProjectorExpected.set(1, 2, 0.292532);
      nullspaceProjectorExpected.set(1, 3,-0.322327);

      nullspaceProjectorExpected.set(2, 0,-0.381917);
      nullspaceProjectorExpected.set(2, 1, 0.292532);
      nullspaceProjectorExpected.set(2, 2, 0.383593);
      nullspaceProjectorExpected.set(2, 3,-0.0708113);

      nullspaceProjectorExpected.set(3, 0,-0.162518);
      nullspaceProjectorExpected.set(3, 1,-0.322327);
      nullspaceProjectorExpected.set(3, 2,-0.0708113);
      nullspaceProjectorExpected.set(3, 3, 0.161357);

      for (int i = 0; i < nullspaceProjectorExpected.getNumElements(); i++)
         assertEquals(nullspaceProjectorExpected.get(i), nullspaceProjector.get(i), 1e-4);
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 30000)
   public void testSimpleProjectOntoNullspace()
   {
      NullspaceProjectorCalculator nullspaceProjectorCalculator = getNullspaceProjectorCalculator();

      DenseMatrix64F jacobian = new DenseMatrix64F(2, 2);
      jacobian.set(0, 0, 1.0);
      jacobian.set(0, 1, 3.0);
      jacobian.set(1, 0, 7.0);
      jacobian.set(1, 1, 9.0);

      DenseMatrix64F vectorToProject = new DenseMatrix64F(1, 2);
      vectorToProject.set(0, 0, 3.5);
      vectorToProject.set(0, 1, 4.5);

      DenseMatrix64F projectedVector = new DenseMatrix64F(1, 2);

      nullspaceProjectorCalculator.projectOntoNullspace(vectorToProject, jacobian, projectedVector);

      assertEquals(0.0, projectedVector.get(0, 0), 1e-7);
      assertEquals(0.0, projectedVector.get(0, 1), 1e-7);


      jacobian = new DenseMatrix64F(2, 4);
      jacobian.set(0, 0, 1.0);
      jacobian.set(0, 1, 3.0);
      jacobian.set(0, 3, 7.0);
      jacobian.set(1, 0, 7.0);
      jacobian.set(1, 2, 9.0);
      jacobian.set(1, 3, 11.0);

      vectorToProject = new DenseMatrix64F(1, 4);
      vectorToProject.set(0, 0, 3.5);
      vectorToProject.set(0, 1, 4.5);
      vectorToProject.set(0, 2, 5.5);
      vectorToProject.set(0, 3, 6.5);

      projectedVector = new DenseMatrix64F(1, 4);

      nullspaceProjectorCalculator.projectOntoNullspace(vectorToProject, jacobian, projectedVector);

      assertEquals(0.0423707, projectedVector.get(0, 0), 1e-4);
      assertEquals(3.15904, projectedVector.get(0, 1), 1e-4);
      assertEquals(1.62918, projectedVector.get(0, 2), 1e-4);
      assertEquals(-1.35993, projectedVector.get(0, 3), 1e-4);

   }
}
