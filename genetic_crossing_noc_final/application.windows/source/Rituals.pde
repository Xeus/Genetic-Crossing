public class Rituals {

  Rituals() {
  }

  void funeral(boolean funeralsRecognized) {
    println(funeralsRecognized);
    for (int i=0; i<numPeople; i++) {
      if (person[i].parent1 != -1 && person[i].parent2 != -1 && person[i].alive == false) {
        if (funeralsRecognized == true) {
          physics.removeSpring(parentSpringArray.get(person[i].parent1Spring));
          physics.removeSpring(parentSpringArray.get(person[i].parent2Spring));
          physics.removeSpring(parentMinDistanceSpringArray.get(person[i].parent1MinDistanceSpring));
          physics.removeSpring(parentMinDistanceSpringArray.get(person[i].parent2MinDistanceSpring));
          person[i].lock();
          person[i].display(0);
        }
        else { // TODO: re-reference spring after it's recreated?
          parentSpringArray.add(new VerletConstrainedSpring2D(person[i], person[person[i].parent1], person[i].parent1RL, random(parentGravity1, parentGravity2)));
          person[i].parent1Spring = parentSpringArray.size()-1;
          physics.addSpring(parentSpringArray.get(parentSpringArray.size()-1));
          parentSpringArray.add(new VerletConstrainedSpring2D(person[i], person[person[i].parent2], person[i].parent2RL, random(parentGravity1, parentGravity2)));
          person[i].parent2Spring = parentSpringArray.size()-1;
          physics.addSpring(parentSpringArray.get(parentSpringArray.size()-1));
          parentMinDistanceSpringArray.add(new VerletMinDistanceSpring2D(person[i], person[person[i].parent1], random(parentMinDistanceRL1, parentMinDistanceRL2), random(parentGravity1, parentGravity2)));
          physics.addSpring(parentMinDistanceSpringArray.get(parentMinDistanceSpringArray.size()-1));
          parentMinDistanceSpringArray.add(new VerletMinDistanceSpring2D(person[i], person[person[i].parent2], random(parentMinDistanceRL1, parentMinDistanceRL2), random(parentGravity1, parentGravity2)));
          physics.addSpring(parentMinDistanceSpringArray.get(parentMinDistanceSpringArray.size()-1));
          person[i].unlock();
          person[i].display(1);
        }
      }
    }
  }
}

