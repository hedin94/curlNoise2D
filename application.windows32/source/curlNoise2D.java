import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class curlNoise2D extends PApplet {

ParticleSystem particleSystem;

public void setup() {
  
  background(0);

  particleSystem = new ParticleSystem();
}

public void draw() {
  fill(0, 5);
  noStroke();
  rect(0, 0, width, height);

  particleSystem.update();
  particleSystem.draw();
  println(floor(frameRate), particleSystem.particles.size());
}
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

  public void update() {
    oldPos = pos.copy();
    pos.add(vel);
    vel.add(acc);
    vel.limit(maxSpeed);
  }

  public void draw() {
    colorMode(HSB, 360, 100, 100, 100);
    stroke(colour, 100, 75, 50);
    line(oldPos.x, oldPos.y, pos.x, pos.y);
    colorMode(RGB, 255, 255, 255, 100);
  }
}
class ParticleSystem {

  float spawnRate = 1;
  float spawnMult = 20;
  float maxSpawns = 10000;

  float fieldWeight = 10;
  float viscosity = 0.8f;

  float t = 0;
  float timeStep = 0.001f;
  float noiseScl = 0.002f;

  ArrayList<Particle> particles;

  ParticleSystem() {
    particles = new ArrayList<Particle>();
  }

  public void update() {
    spawn();
    cleanup();
    for (Particle p : particles) {
      PVector pos = p.pos.copy();
      pos.mult(noiseScl);
      pos.z = t;
      p.acc.add(curl2D(pos).mult(fieldWeight));
      p.acc.mult(1-viscosity);
      p.update();
    }
    t += timeStep;
  }

  public void draw() {
    for (Particle p : particles)
      p.draw();
  }

  public void spawn() {
    if (particles.size() < maxSpawns)
      if (random(1) < spawnRate)
        for (int i = 0; i < spawnMult; i++)
          particles.add(new Particle());
  }

  public void cleanup() {
    int i = 0;
    while (i < particles.size()) {
      PVector pos = particles.get(i).pos;
      if (pos.x < 0 || pos.x > width || pos.y < 0 || pos.y > height)
        particles.remove(i);
      else
        i++;
    }
  }

  public PVector curl2D(float x, float y) {
    return curl2D(new PVector(x, y));
  }

  public PVector curl2D(float x, float y, float z) {
    return curl2D(new PVector(x, y, z));
  }

  public PVector curl2D(PVector v) {
    float e = 0.005f;
    float n1, n2, x, y;

    n1 = noise(v.x, v.y + e, v.z);
    n2 = noise(v.x, v.y - e, v.z);
    x = (n1-n2)/(2*e);

    n1 = noise(v.x + e, v.y, v.z);
    n2 = noise(v.x - e, v.y, v.z);
    y = (n1-n2)/(2*e);

    return new PVector(x, -y);
  }
}
  public void settings() {  size(800, 600, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "curlNoise2D" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
