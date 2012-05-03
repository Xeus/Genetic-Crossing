public class MatingDance {

  MatingDance() {
  }

  // gets two unique male-female pairs to test compatibility
  void flirt() {
    boolean twoUniques = false;
    int randPpl = numPeople - 1;
    flirter1 = (int)random(randPpl);
    while (twoUniques == false) {
      flirter2 = (int)random(randPpl);
      if (flirter1 != flirter2) {
        twoUniques = true;
      }
    }
  }

  // takes two results from flirt() and sees if they pass certain reproduction tests
  // e.g. male-female pair? similar appearance level? same level of money?
  // then returns boolean saying whether sex() will occur
  boolean chemistry(int _flirter1, int _flirter2) {
    boolean procreate = false;
    int randomTest = (int)random(10);
    if (randomTest < chemistryTolerance) {
      // lots of rules
      if (checkFertile(_flirter1, _flirter2)) {
        if ((person[_flirter1].gender == "male" && person[_flirter2].gender == "female") || (person[_flirter2].gender == "male" && person[_flirter1].gender == "female")) {
          if (abs((int)Float.parseFloat(person[_flirter1].trait.get("appearance").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("appearance").toString())) <= chemistryAppearanceTolerance) {
            if (abs((int)Float.parseFloat(person[_flirter1].trait.get("money").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("money").toString())) <= chemistryMoneyTolerance) {
              if (abs((int)Float.parseFloat(person[_flirter1].trait.get("religiosity").toString())) - abs((int)Float.parseFloat(person[_flirter2].trait.get("religiosity").toString())) <= chemistryReligiosityTolerance) {
                procreate = true;
              }
            }
          }
        }
      }
    }
    return procreate;
  }

  // takes two parents and creates an offspring person object
  void sex(Person comp1, Person comp2) {
    Person newPerson;
    String _gender = "";
    int _pickGender = (int)random(0, 1.99);
    int _parent1 = comp1.uniqueID;
    int _parent2 = comp2.uniqueID;
    String _nationality;
    switch (_pickGender) {
    case 0:
      _gender = "male";
      break;
    case 1:
      _gender = "female";
      break;
    } 

    int _getNationality = (int)random(0, 4.99);
    int[] percentage = {
      0, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 4, 5, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 10, 10, 10, 10, 10, 10, 11, 11, 11, 11, 11, 11, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 14, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15
    }; 
    int _getMbti = (int)random(0, 99.99);

    // #PeopleChange
    newPerson = new Person(numPeople, percentage[_getMbti], 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (int)random(90, 400), (int)random(48, 96), 
    0, 1, 0, 0, 0, 0, 0, 0, 0, 0, "person"+Integer.toString(numPeople+1), _gender, _parent1, 
    _parent2, _getNationality, currentYear, (int)random(0, 6.99), 0, (int)random(0, 1.99), randomX(_getNationality), randomY(_getNationality));

    Object characteristic, characteristic2;
    for (Enumeration e = comp1.trait.keys() ; e.hasMoreElements() ;) {
      characteristic = e.nextElement();
      int compy1 = (int)(Float.parseFloat(comp1.trait.get(characteristic).toString()));
      int compy2 = (int)(Float.parseFloat(comp2.trait.get(characteristic).toString()));
      newPerson.trait.put(characteristic, punnettSquare(compy1, compy2));
    }

    person[numPeople] = newPerson;

    // takes parents' traits and combines them to see what child will get
    if (person[numPeople].parent1 != -1 && person[numPeople].parent2 != -1) {
      person[numPeople].strength = punnettSquare(person[_parent1].strength, person[_parent2].strength);
      person[numPeople].intelligence = punnettSquare(person[_parent1].intelligence, person[_parent2].intelligence);
      person[numPeople].wisdom = punnettSquare(person[_parent1].wisdom, person[_parent2].wisdom);
      person[numPeople].charisma = punnettSquare(person[_parent1].charisma, person[_parent2].charisma);
      person[numPeople].stamina = punnettSquare(person[_parent1].stamina, person[_parent2].stamina);
      person[numPeople].wit = punnettSquare(person[_parent1].wit, person[_parent2].wit);
      person[numPeople].humor = punnettSquare(person[_parent1].humor, person[_parent2].humor);
      person[numPeople].education = punnettSquare(person[_parent1].education, person[_parent2].education);
      person[numPeople].creativity = punnettSquare(person[_parent1].creativity, person[_parent2].creativity);
      person[numPeople].responsibility = punnettSquare(person[_parent1].responsibility, person[_parent2].responsibility);
      person[numPeople].discipline = punnettSquare(person[_parent1].discipline, person[_parent2].discipline);
      person[numPeople].honesty = punnettSquare(person[_parent1].honesty, person[_parent2].honesty);
      person[numPeople].religiosity = punnettSquare(person[_parent1].religiosity, person[_parent2].religiosity);
      person[numPeople].entrepreneurialism = punnettSquare(person[_parent1].entrepreneurialism, person[_parent2].entrepreneurialism);
      person[numPeople].appearance = punnettSquare(person[_parent1].appearance, person[_parent2].appearance);
      person[numPeople].money = punnettSquare(person[_parent1].money, person[_parent2].money);
      person[numPeople].gracefulness = punnettSquare(person[_parent1].gracefulness, person[_parent2].gracefulness);
      person[numPeople].stress = punnettSquare(person[_parent1].stress, person[_parent2].stress);
      person[numPeople].health = punnettSquare(person[_parent1].health, person[_parent2].health);
      person[numPeople].luck = punnettSquare(person[_parent1].luck, person[_parent2].luck);
      person[numPeople].talentMath = punnettSquare(person[_parent1].talentMath, person[_parent2].talentMath);
      person[numPeople].talentArt = punnettSquare(person[_parent1].talentArt, person[_parent2].talentArt);
      person[numPeople].talentSports = punnettSquare(person[_parent1].talentSports, person[_parent2].talentSports);
    }

    // add new person to physics world with appropriate springs
    physics.addParticle(person[numPeople]);
    godSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], god, godRL, godGravity));

    // creates random rest length, saves to Person object and makes new spring between person and nation
    int tempNationRL = round(random(nationRL1, nationRL2));
    nationSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], nation[person[numPeople].nationality], tempNationRL, random(nationGravity1, nationGravity2)));
    physics.addSpring(nationSpringArray.get(nationSpringArray.size()-1));
    person[numPeople].nationRL = tempNationRL;
    if (person[numPeople].parent1 != -1 && person[numPeople].parent2 != -1) {
      int tempParent1RL = round(random(parentRL1, parentRL2));
      int tempParent2RL = round(random(parentRL1, parentRL2));
      parentSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], person[person[numPeople].parent1], tempParent1RL, random(parentGravity1, parentGravity2)));
      physics.addSpring(parentSpringArray.get(parentSpringArray.size()-1));
      person[numPeople].parent1Spring = parentSpringArray.size()-1;
      parentSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], person[person[numPeople].parent2], tempParent1RL, random(parentGravity1, parentGravity2)));
      physics.addSpring(parentSpringArray.get(parentSpringArray.size()-1));
      person[numPeople].parent2Spring = parentSpringArray.size()-1;
      person[numPeople].parent1RL = tempParent1RL;
      person[numPeople].parent2RL = tempParent2RL;
      parentMinDistanceSpringArray.add(new VerletMinDistanceSpring2D(person[numPeople], person[person[numPeople].parent1], random(parentMinDistanceRL1, parentMinDistanceRL2), random(parentGravity1, parentGravity2)));
      person[numPeople].parent1MinDistanceSpring = parentMinDistanceSpringArray.size()-1;
      physics.addSpring(parentMinDistanceSpringArray.get(parentMinDistanceSpringArray.size()-1));
      parentMinDistanceSpringArray.add(new VerletMinDistanceSpring2D(person[numPeople], person[person[numPeople].parent2], random(parentMinDistanceRL1, parentMinDistanceRL2), random(parentGravity1, parentGravity2)));
      person[numPeople].parent2MinDistanceSpring = parentMinDistanceSpringArray.size()-1;
      physics.addSpring(parentMinDistanceSpringArray.get(parentMinDistanceSpringArray.size()-1));
    }
    int tempReligionRL = round(random(religionRL1, religionRL2));
    religionSpringArray.add(new VerletConstrainedSpring2D(person[numPeople], religion[person[numPeople].religion], tempReligionRL, random(religionGravity1, religionGravity2)));
    physics.addSpring(religionSpringArray.get(religionSpringArray.size()-1));
    person[numPeople].religionRL = tempReligionRL;
    if (person[numPeople].parent1 != -1 && person[numPeople].parent2 != -1) {
      person[_parent1].children++;
      person[_parent2].children++;
    }
    numPeople++;
    populationSize++;
  }

  boolean beer(int _flirter1, int _flirter2) {
    if ((abs((int)Float.parseFloat(person[_flirter1].trait.get("appearance").toString()) - (int)Float.parseFloat(person[_flirter2].trait.get("appearance").toString())) <= 2) && checkFertile(_flirter1, _flirter2)) {
      return true;
    }
    else { 
      return false;
    }
  }

  // can the person have a baby now?
  boolean checkFertile(int _flirter1, int _flirter2) {
    int female = 0;
    boolean fertile = false;
    if (person[_flirter1].gender == "female" && person[_flirter1].alive == true) {
      female = 1;
      if ((currentYear - person[_flirter1].lastBaby > gestationRate) && (person[_flirter1].age >= 18) && (person[_flirter1].age <= maxReproductiveAge) && (person[_flirter2].age >= 18)) {
        fertile = true;
      }
    }
    else if (person[_flirter2].gender == "female" && person[_flirter1].alive == true) {
      female = 2;
      if ((currentYear - person[_flirter2].lastBaby > gestationRate) && (person[_flirter2].age >= 18) && (person[_flirter2].age <= maxReproductiveAge) && (person[_flirter1].age >= 18)) {
        fertile = true;
      }
    }
    return fertile;
  }

//  int punnettSquare(int comp1, int comp2) {
//    int mutation = (int)random(0, 9.99);
//    int offspringTraitVal = ceil((comp1 + comp2) / 2);
//    if (mutation < 3) {
//      offspringTraitVal += (int)random(-2, 2);
//    } 
//    if (offspringTraitVal < 1) { 
//      offspringTraitVal = 1;
//    }
//    if (offspringTraitVal > 10) { 
//      offspringTraitVal = 10;
//    }
//    return offspringTraitVal;
//  }

  // modified this from the above, which just averages trait values
  // this function will decide whether to take the either parent's full value and then see if it mutates an offset of, say, 4 points
  int punnettSquare(int comp1, int comp2) {
    int mutation = (int)random(0, 50);
    int dominance = floor(random(0, 1.99));
    int crossover = 0;
    if (dominance == 0) {
      crossover = comp1;
    } 
    else {
      crossover = comp2;
    }
    if (mutation < 2) {
      crossover += (int)random(-4, 4);
    }
    // don't want it to be out of bounds
    // TODO: fix so it can mutate only one way if parent is 1 or 10
    if (crossover < 1) { crossover = 1; } else if (crossover > 10) { crossover = 10; }
    return crossover;
  }
}

