
package example.payment.jbricks;
import java.util.*;
import javax.microedition.lcdui.Image;
import de.enough.polish.util.BitMapFont;
import de.enough.polish.util.BitMapFontViewer;
import de.enough.polish.util.*;
import de.enough.polish.math.FP;


public class Vector2D {
    
    static int  CARTESIAN = 0;
    static int  POLAR = 1;
      
    static double EPSILON  = 0.001;
    double x=0;   /**< x-coordinate of this position */
    double y=0;   /**< y-coordinate of this position */
    int cs;

    public  Vector2D(double dX, double dY) {
       x =(dX); 
       y =(dY);
    }
    
    public  Vector2D(int dX, int dY) {
       x =(double)(dX); 
       y =(double)(dY);
    } 

    public  Vector2D( int dX, float dY, int cs) {
      if( cs == this.CARTESIAN )  
      { 
        x =(double)(dX); 
        y =(double)(dY); 
        this.cs=cs;
      }
      else
        getVector2DFromPolar( (double)dX, (double)dY );
    }
    
    double getX(){
        return x;
    }
    
    double getY(){
        return y;
    }
    
    void setY(double y) {
        this.y = y;
    }

    
    void setX(double x) {
           this.x = x;
    }

    
    void setVector2D( int dX, float dY, int cs){
      if( cs == this.CARTESIAN )  
      { 
        x =(double)(dX); 
        y =(double)(dY); 
        this.cs=cs;
      }
      else
        getVector2DFromPolar( (double)dX, (double)dY );
    }
    
    
    
    Vector2D setLength( float d ){
      if( getLength( ) > EPSILON ) {
        double l = getLength( );
        this.x  *= ( d / l );
        
        this.y  *= ( d / l );

      }
      return (this );
    }
    
    void rotate( float angle ) {
      // determine the polar representation of the current Vector2D
      int dMag    = (int)getLength( );
      float dNewDir = (float)getDirection( ) + angle;  // add rotation angle to phi
      setVector2D( dMag, dNewDir, this.POLAR );          // convert back to Cartesian
    }
    
 
    double getDirection() {
        
        if( Math.abs(x) < EPSILON && Math.abs( y ) < EPSILON )
          return ( 0.0F );
        return mMath.atan2(y, x)*180/Math.PI;
    }
  
  double distanceTo(Vector2D p) { 
    return ( ( this.minusNew(p)).getLength( ) ); 
  }

  public Vector2D minusNew(Vector2D p){
    return new Vector2D(this.x-p.x, this.y-p.y); 
  }
  public Vector2D minusNew(double p){
    return new Vector2D(this.x-p, this.y-p); 
  }
  public Vector2D addNew(Vector2D p){
    return new Vector2D(this.x+p.x, this.y+p.y); 
  }
  
  public Vector2D multiNew(Vector2D p){
    return new Vector2D(this.x*p.x, this.y*p.y); 
    
  }
  
  public Vector2D multiNew(double p){
     return new Vector2D(this.x*p, this.y*p); 
     
  }

  
  public Vector2D minus(Vector2D p){
    this.x -=p.x;
    this.y -=p.y; 
    return this;
  }
  
  public Vector2D add(Vector2D p){
     this.x +=p.x;
    this.y +=p.y; 
    return this;
  }
  public Vector2D div(double p){
    this.x /=p;
    this.y /=p; 
    return this;
  }
  public void multi(Vector2D p){
     this.x *=p.x;
    this.y *=p.y; 
  }

  
  double  getLength ()  {
    return ( Math.sqrt( 
        x * x + y * y ) ); 
  }
    
  void getVector2DFromPolar( double dMag, double ang ) {
     ang = ang*Math.PI/180;
    // cos(phi) = x/r <=> x = r*cos(phi); sin(phi) = y/r <=> y = r*sin(phi)
     this.x= dMag* Math.cos( ang );
     this.y = dMag * Math.sin( ang )  ;
  }


    
}
class Circle{
  Vector2D posCenter;            /**< Center of the circle  */
  float    radius;               /**< Radius of the circle  */
  
  Circle( Vector2D pos, float dR ){
    setCircle( pos, dR );
  }
  
  Circle(){
    setCircle(new Vector2D(-1000.0,-1000.0), 0);
  }
  
  
  boolean setCircle(Vector2D pos, float dR ) {
    posCenter = pos;
    return setRadius(dR);
  }
  
  boolean setRadius( float dR ){
    if( dR > 0 ) {
      radius = dR;
      return true;
    }
    else {
      radius = 0.0F;
      return false;
    }
  }
  
  //#if 0
  int getIntersectionPoints(Circle c, Vector2D p1,
                     Vector2D p2)  {
      float x0, y0, r0;
      float x1, y1, r1;
  
      x0 = getCenter( ).getX();
      y0 = getCenter( ).getY();
      r0 = getRadius( );
      x1 = c.getCenter( ).getX();
      y1 = c.getCenter( ).getY();
      r1 = c.getRadius( );
  
      float      d, dx, dy, h, a, x, y, p2_x, p2_y;
  
      // first calculate distance between two centers circles P0 and P1.
      dx = x1 - x0;
      dy = y1 - y0;
      d = Math.sqrt(dx*dx + dy*dy);
  
      // normalize differences
      dx /= d; dy /= d;
  
      // a is distance between p0 and point that is the intersection point P2
      // that intersects P0-P1 and the line that crosses the two intersection
      // points P3 and P4.
      // Define two triangles: P0,P2,P3 and P1,P2,P3.
      // with distances a, h, r0 and b, h, r1 with d = a + b
      // We know a^2 + h^2 = r0^2 and b^2 + h^2 = r1^2 which then gives
      // a^2 + r1^2 - b^2 = r0^2 with d = a + b ==> a^2 + r1^2 - (d-a)^2 = r0^2
      // ==> r0^2 + d^2 - r1^2 / 2*d
      a = (r0*r0 + d*d - r1*r1) / (2.0 * d);
  
      // h is then a^2 + h^2 = r0^2 ==> h = sqrt( r0^2 - a^2 )
      float      arg = r0*r0 - a*a;
      h = (arg > 0.0) ? sqrt(arg) : 0.0;
  
      // First calculate P2
      p2_x = x0 + a * dx;
      p2_y = y0 + a * dy;
  
      // and finally the two intersection points
      x =  p2_x - h * dy;
      y =  p2_y + h * dx;
      p1->setVector2D( x, y );
      x =  p2_x + h * dy;
      y =  p2_y - h * dx;
      p2->setVector2D( x, y );
  
      return (arg < 0.0) ? 0 : ((arg == 0.0 ) ? 1 :  2);
  }
  
  
  float getIntersectionArea(  Circle c )  {
    Vector2D pos1, pos2, pos3;
    float d, h, dArea;
    AngDeg ang;
  
    d = getCenter().distanceTo( c.getCenter() ); // dist between two centers
    if( d > c.getRadius() + getRadius() )           // larger than sum radii
      return 0.0;                                   // circles do not intersect
    if( d <= fabs(c.getRadius() - getRadius() ) )   // one totally in the other
    {
      float dR = min( c.getRadius(), getRadius() );// return area smallest circle
      return M_PI*dR*dR;
    }
  
    int iNrSol = getIntersectionPoints( c, &pos1, &pos2 );
    if( iNrSol != 2 )
      return 0.0;
  
    // the intersection area of two circles can be divided into two segments:
    // left and right of the line between the two intersection points p1 and p2.
    // The outside area of each segment can be calculated by taking the part
    // of the circle pie excluding the triangle from the center to the
    // two intersection points.
    // The pie equals pi*r^2 * rad(2*ang) / 2*pi = 0.5*rad(2*ang)*r^2 with ang
    // the angle between the center c of the circle and one of the two
    // intersection points. Thus the angle between c and p1 and c and p3 where
    // p3 is the point that lies halfway between p1 and p2.
    // This can be calculated using ang = asin( d / r ) with d the distance
    // between p1 and p3 and r the radius of the circle.
    // The area of the triangle is 2*0.5*h*d.
  
    pos3 = pos1.getVector2DOnLineFraction( pos2, 0.5 );
    d = pos1.distanceTo( pos3 );
    h = pos3.distanceTo( getCenter() );
    ang = asin( d / getRadius() );
  
    dArea = ang*getRadius()*getRadius();
    dArea = dArea - d*h;
  
    // and now for the other segment the same story
    h = pos3.distanceTo( c.getCenter() );
    ang = asin( d / c.getRadius() );
    dArea = dArea + ang*c.getRadius()*c.getRadius();
    dArea = dArea - d*h;
  
    return dArea;
  }
  
  
  boolean calcTangentIntersectionPoints( Vector2D startPoint, Vector2D &point1, Vector2D &point2){
    if(isInside(startPoint)){
      // Startpoint is inside circle -> there are no tangent interception points
      return(false);
    }
  
    //float d = posCenter.getLength()-startPoint.getLength();
    float d = (posCenter-startPoint).getLength();
    float r = radius;
  
    float alphaRad = asin(r/d);
  
    float p = sqrt(d*d-r*r);
  
    point1.setX(cos(alphaRad)*p);
    point1.setY(sin(alphaRad)*p);
    point2.setX(cos(-alphaRad)*p);
    point2.setY(sin(-alphaRad)*p);
    
    point1=point1.rotate((posCenter-startPoint).getDirection());
    point2=point2.rotate((posCenter-startPoint).getDirection());
  
    point1+=startPoint;
    point2+=startPoint;
  
    return(true);
  }
  //#endif
  float getRadius ()         { return radius; }
  Vector2D getCenter ()      { return posCenter; }
  float getCircumference ()  { return 2.0F*(float)Math.PI*getRadius(); }
  float getArea ()           { return (float)Math.PI*getRadius()*getRadius(); }

  boolean isInside (Vector2D pos ) 
  { 
    return posCenter.distanceTo(pos) < getRadius(); 
  }

}


