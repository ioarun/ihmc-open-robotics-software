package us.ihmc.graphics3DAdapter.utils;

import us.ihmc.graphics3DAdapter.graphics.Graphics3DObject;
import us.ihmc.graphics3DAdapter.structure.Graphics3DNode;
import us.ihmc.graphics3DAdapter.structure.Graphics3DNodeType;

public class CoordinateFrameNode extends Graphics3DNode
{
   public static final double ONE_UNIT = 1.0;
   
   public CoordinateFrameNode()
   {
      super(CoordinateFrameNode.class.getSimpleName(), Graphics3DNodeType.VISUALIZATION);
      
      Graphics3DObject coordinateFrameObject = new Graphics3DObject();
      coordinateFrameObject.addCoordinateSystem(ONE_UNIT);
      
      setGraphicsObject(coordinateFrameObject);
   }
}
