class ParticleSystem {

  float spawnRate = 1;
  float spawnMult = 20;
  float maxSpawns = 10000;

  float fieldWeight = 10;
  float viscosity = 0.8;

  float t = 0;
  float timeStep = 0.001;
  float noiseScl = 0.002;

  ArrayList<Particle> particles;

  ParticleSystem() {
    particles = new ArrayList<Particle>();
  }

  void update() {
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

  void draw() {
    for (Particle p : particles)
      p.draw();
  }

  void spawn() {
    if (particles.size() < maxSpawns)
      if (random(1) < spawnRate)
        for (int i = 0; i < spawnMult; i++)
          particles.add(new Particle());
  }

  void cleanup() {
    int i = 0;
    while (i < particles.size()) {
      PVector pos = particles.get(i).pos;
      if (pos.x < 0 || pos.x > width || pos.y < 0 || pos.y > height)
        particles.remove(i);
      else
        i++;
    }
  }

  PVector curl2D(float x, float y) {
    return curl2D(new PVector(x, y));
  }

  PVector curl2D(float x, float y, float z) {
    return curl2D(new PVector(x, y, z));
  }

  PVector curl2D(PVector v) {
    float e = 0.005;
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