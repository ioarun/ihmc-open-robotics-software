package us.ihmc.aware.input.value;

public class InputValueIntegrator
{
   private final double dt;
   private double value;

   public InputValueIntegrator(double dt, double value)
   {
      this.dt = dt;
      this.value = value;
   }

   public double update(double dv)
   {
      value += dv * dt;
      return value;
   }

   public void reset()
   {
      value = 0.0;
   }

   public double value()
   {
      return value;
   }
}
