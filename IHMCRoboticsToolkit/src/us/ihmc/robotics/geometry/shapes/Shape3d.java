package us.ihmc.robotics.geometry.shapes;

import us.ihmc.euclid.interfaces.GeometryObject;
import us.ihmc.euclid.interfaces.Transformable;
import us.ihmc.euclid.matrix.RotationMatrix;
import us.ihmc.euclid.matrix.interfaces.RotationMatrixReadOnly;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.transform.interfaces.Transform;
import us.ihmc.euclid.tuple3D.interfaces.Point3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;
import us.ihmc.euclid.tuple4D.interfaces.QuaternionBasics;
import us.ihmc.euclid.tuple4D.interfaces.QuaternionReadOnly;
import us.ihmc.euclid.tuple4D.interfaces.Vector4DBasics;
import us.ihmc.euclid.tuple4D.interfaces.Vector4DReadOnly;
import us.ihmc.robotics.math.Epsilons;

public abstract class Shape3d<S extends Shape3d<S>> implements GeometryObject<S>
{
   private final TwoWayRigidBodyTransform twoWayTransform;
   
   protected Shape3d()
   {
      twoWayTransform = new TwoWayRigidBodyTransform();
   }
   
   /**
    * Find the distance from the closest point on this shape to the given point. Returns 0.0 if the point is inside.
    *
    * @param point
    * @return distance from the point to this Shape3d.
    */
   public final double distance(Point3DBasics point)
   {
      transformToLocal(point);
      double distance = distanceShapeFrame(point);
      transformToWorld(point);
      return distance;
   }
   
   protected abstract double distanceShapeFrame(Point3DReadOnly point);

   /**
    * Determine whether the given point is on or inside the surface of this shape.
    *
    * @param pointToCheck
    * @return true if the point is inside or on the surface, false otherwise.
    */
   public final boolean isInsideOrOnSurface(Point3DBasics pointToCheck)
   {
      return isInsideOrOnSurface(pointToCheck, Epsilons.ONE_TRILLIONTH);
   }

   /**
    * Determine whether the given point is on or inside the surface of this shape,
    * within a given tolerance or error level. If epsilonToGrowObject is positive, then 
    * the object will be checked as being larger. If negative, then the object will be shrunk.
    *
    * @param pointToCheck
    * @param epsilon
    * @return
    */
   public final boolean isInsideOrOnSurface(Point3DBasics pointToCheck, double epsilonToGrowObject)
   {
      transformToLocal(pointToCheck);
      boolean isInsideOrOnSurface = isInsideOrOnSurfaceShapeFrame(pointToCheck, epsilonToGrowObject);
      transformToWorld(pointToCheck);
      return isInsideOrOnSurface;
   }
   
   protected abstract boolean isInsideOrOnSurfaceShapeFrame(Point3DReadOnly pointToCheck, double epsilonToGrowObject);

   /**
    * Find the closest point on the surface of this shape to the given point.
    * If the given point is on or inside the shape, then it is not changed.
    * If you wish to know the surface normal, then subtract the projected point from the original point.
    *
    * @param pointToCheckAndPack both an input parameter (the point to check),
    *          and an output parameter (packed with the resulting orthogonal point).
    */
   public final void orthogonalProjection(Point3DBasics pointToCheckAndPack)
   {
      transformToLocal(pointToCheckAndPack);
      orthogonalProjectionShapeFrame(pointToCheckAndPack);
      transformToWorld(pointToCheckAndPack);
   }
   
   protected abstract void orthogonalProjectionShapeFrame(Point3DBasics pointToCheckAndPack);

   /**
    * Returns true if inside the Shape3d. If inside, must pack the intersection and normal. If not inside, packing those is optional.
    * But if they are not packed when outside, then they should be set to NaN. If they are set to NaN and you really do wish to see
    * where they would project to, then call orthogonalProjection.
    *
    * @param pointToCheck
    * @param intersectionToPack
    * @param normalToPack
    * @return true if the point is inside, false otherwise.
    */
   public final boolean checkIfInside(Point3DBasics pointToCheck, Point3DBasics closestPointOnSurfaceToPack, Vector3DBasics normalToPack)
   {
      transformToLocal(pointToCheck);
      boolean isInside = checkIfInsideShapeFrame(pointToCheck, closestPointOnSurfaceToPack, normalToPack);
      //TODO: This modifies pointToCheck and transforms back. Should we make a temp variable instead, or are we trying to be Thread safe here?
      transformToWorld(pointToCheck);
      if (closestPointOnSurfaceToPack != null)
      {
         transformToWorld(closestPointOnSurfaceToPack);
      }
      if (normalToPack != null)
      {
         transformToWorld(normalToPack);
      }
      return isInside;
   }
   
   protected abstract boolean checkIfInsideShapeFrame(Point3DReadOnly pointToCheck, Point3DBasics closestPointOnSurfaceToPack, Vector3DBasics normalToPack);
   
   // Transform getters
   
   public void getTransform(RigidBodyTransform transformToPack)
   {
      transformToPack.set(getTransformToWorldUnsafe());
   }
   
   public RigidBodyTransform getTransformUnsafe()
   {
      return getTransformToWorldUnsafe();
   }

   public void getPosition(Point3DBasics positionToPack)
   {
      getTransformToWorldUnsafe().getTranslation(positionToPack);
   }
   
   public void getOrientation(RotationMatrix orientationToPack)
   {
      getTransformToWorldUnsafe().getRotation(orientationToPack);
   }
   
   public void getOrientation(QuaternionBasics orientationToPack)
   {
      getTransformToWorldUnsafe().getRotation(orientationToPack);
   }
   
   // Transform setters
   
   @Override
   public void applyTransform(Transform transform)
   {
      RigidBodyTransform fromShape = getTransformToWorldUnsafe();
      transform.transform(fromShape);
      setTransformToWorld(fromShape);
   }

   public void appendTransform(RigidBodyTransform transform)
   {
      RigidBodyTransform fromShape = getTransformFromShapeFrameUnsafe();
      fromShape.multiply(transform);
      setTransformFromShapeFrame(fromShape);
   }

   public void setPosition(Point3DReadOnly point)
   {
      RigidBodyTransform fromShape = getTransformToWorldUnsafe();
      fromShape.setTranslation(point);
      setTransformToWorld(fromShape);
   }

   public void setPosition(double x, double y, double z)
   {
      RigidBodyTransform fromShape = getTransformToWorldUnsafe();
      fromShape.setTranslation(x, y, z);
      setTransformToWorld(fromShape);
   }
   
   public void setOrientation(QuaternionReadOnly orientation)
   {
      RigidBodyTransform fromShape = getTransformToWorldUnsafe();
      fromShape.setRotation(orientation);
      setTransformToWorld(fromShape);
   }

   public void setOrientation(RotationMatrixReadOnly orientation)
   {
      RigidBodyTransform fromShape = getTransformToWorldUnsafe();
      fromShape.setRotation(orientation);
      setTransformToWorld(fromShape);
   }

   public void setOrientation(double yaw, double pitch, double roll)
   {
      RigidBodyTransform fromShape = getTransformToWorldUnsafe();
      fromShape.setRotationYawPitchRoll(yaw, pitch, roll);
      setTransformToWorld(fromShape);
   }
   
   public void setPose(Point3DReadOnly position, QuaternionReadOnly orientation)
   {
      RigidBodyTransform fromShape = getTransformToWorldUnsafe();
      fromShape.set(orientation, position);
      setTransformToWorld(fromShape);
   }
   
   public void setTransform(RigidBodyTransform transform)
   {
      setTransformToWorld(transform);
   }
   
   // Two way transform boilerplate

   public void setTransformToLocal(RigidBodyTransform transform)
   {
      twoWayTransform.setForwardTransform(transform);
   }
   
   public void setTransformToWorld(RigidBodyTransform transform)
   {
      twoWayTransform.setBackwardTransform(transform);
   }
   
   public RigidBodyTransform getTransformToLocalUnsafe()
   {
      return twoWayTransform.getForwardTransformUnsafe();
   }
   
   public RigidBodyTransform getTransformToWorldUnsafe()
   {
      return twoWayTransform.getBackwardTransformUnsafe();
   }
   
   public void ensureBaseTransformsUpToDate()
   {
      twoWayTransform.ensureTransformsUpToDate();
   }
   
   public void transformToLocal(Transformable transformable)
   {
      twoWayTransform.transformForward(transformable);
   }

   public void transformToLocal(Vector3DReadOnly vectorIn, Vector3DBasics vectorOut)
   {
      twoWayTransform.transformForward(vectorIn, vectorOut);
   }
   
   public void transformToLocal(Vector4DReadOnly vectorIn, Vector4DBasics vectorOut)
   {
      twoWayTransform.transformForward(vectorIn, vectorOut);
   }
   
   public void transformToLocal(Point3DReadOnly pointIn, Point3DBasics pointOut)
   {
      twoWayTransform.transformForward(pointIn, pointOut);
   }
   
   public void transformToWorld(Transformable transformable)
   {
      twoWayTransform.transformBackward(transformable);
   }

   public void transformToWorld(Vector3DReadOnly vectorIn, Vector3DBasics vectorOut)
   {
      twoWayTransform.transformBackward(vectorIn, vectorOut);
   }
   
   public void transformToWorld(Vector4DReadOnly vectorIn, Vector4DBasics vectorOut)
   {
      twoWayTransform.transformBackward(vectorIn, vectorOut);
   }
   
   public void transformToWorld(Point3DReadOnly pointIn, Point3DBasics pointOut)
   {
      twoWayTransform.transformBackward(pointIn, pointOut);
   }
}
