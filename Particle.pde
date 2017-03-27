class Particle {
  PVector pos;
  PVector vel;
  PVector acc;
  PVector oldPos;
  
  float maxSpeed = 2;
  
  float colour;
  
  Particle() {
   vel = new PVector(0,0);
   acc = new PVector(0,0);
   //pos = new PVector(random(width), random(height));
   pos = new PVector(width/2-20+random(40), height/2-20+random(40));
   oldPos = pos.copy();
   colour = 250 + random(30);
  }

  void update() {
    oldPos = pos.copy();
    pos.add(vel);
    vel.add(acc);
    vel.limit(maxSpeed);
  }

  void draw() {
    colorMode(HSB, 360, 100, 100, 100);
    stroke(colour, 100, 75, 50);
    //fill(colour);
    line(oldPos.x, oldPos.y, pos.x, pos.y);
    //point(pos.x, pos.y);
    colorMode(RGB, 255, 255, 255, 100);
  }
}