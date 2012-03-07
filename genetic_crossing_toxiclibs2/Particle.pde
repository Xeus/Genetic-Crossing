class Particle extends VerletParticle2D {

  Particle(float x, float y) {
    super(x,y);
  }

  // All we're doing really is adding a display() function to a VerletParticle
  void display() {
    fill(175);
    stroke(0);
    ellipse(x,y,16,16);
  }
}
