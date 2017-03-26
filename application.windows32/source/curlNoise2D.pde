ParticleSystem particleSystem;

void setup() {
  size(800, 600, P2D);
  background(0);

  particleSystem = new ParticleSystem();
}

void draw() {
  fill(0, 5);
  noStroke();
  rect(0, 0, width, height);

  particleSystem.update();
  particleSystem.draw();
  println(floor(frameRate), particleSystem.particles.size());
}