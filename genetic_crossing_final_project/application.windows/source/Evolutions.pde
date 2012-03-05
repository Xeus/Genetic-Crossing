public class Evolutions {

  Evolutions() {
  }

  // basic stats
  int personCharsRawTotal(int i) {
    int personCharsTotal = 0;
    for (Enumeration e = person[i].trait.keys() ; e.hasMoreElements() ;) {
      Object characteristic = e.nextElement();
      Object charVal = person[i].trait.get(characteristic);
      personCharsTotal += int(Float.parseFloat(charVal.toString()));
    }
    return personCharsTotal;
  }
  
  // calc a person's happiness
  int happiness(int i) {
    int happiness = person[i].health + person[i].money + person[i].stress + person[i].creativity + person[i].religiosity +
    round(nation[person[i].nationality].standardOfLiving / 10) + round(nation[person[i].nationality].pollution / 10) +
    round(nation[person[i].nationality].security / 10) + round(nation[person[i].nationality].crime / 10) + person[i].employed * 10;
    return happiness;
  }

  // what stats a person actually has
  int actualTotal(int i) {
    int pCAT = 0;
    int strength = 0, intelligence = 0, education = 0, creativity = 0, religiosity = 0, entrepreneurialism = 0, money = 0, stress = 0, health = 0;

    strength = round((person[i].strength * 10 + int((nation[person[i].nationality].pollution) * (nation[person[i].nationality].nutrition) * person[i].health)) / 1000);
    intelligence = person[i].intelligence * 10 + int((person[i].education) * (nation[person[i].nationality].education));
    education = round((person[i].education * 10 + int((nation[person[i].nationality].education) * (nation[person[i].nationality].standardOfLiving) * (nation[person[i].nationality].crime))) / 10000);
    creativity = person[i].creativity * 10 + int((nation[person[i].nationality].innovation) * (person[i].education * 5));
    religiosity = person[i].religiosity * 10 + int((religion[person[i].religion].morality * 3) * (nation[person[i].nationality].education));
    entrepreneurialism = round((person[i].entrepreneurialism * 10 + int((nation[person[i].nationality].innovation) * (nation[person[i].nationality].jobOpportunity) *
      (nation[person[i].nationality].immigrationPolicy))) / 10000);
    money = round((person[i].money * 10 + int((nation[person[i].nationality].jobOpportunity * 5) * (person[i].education * 5) * (person[i].intelligence * 10) * (person[i].creativity * 15)
      * (person[i].luck * 20) * (religion[person[i].religion].commercial * 2))) / 1000000000);
    stress = round((person[i].stress * 10 + int((person[i].health * 5) * (nation[person[i].nationality].security) * (nation[person[i].nationality].politicalFreedom) *
      (nation[person[i].nationality].nutrition * 2) * (religion[person[i].religion].morality * 2))) / 100000000);
    health = round((person[i].health * 10 + int((nation[person[i].nationality].pollution * 2) * (nation[person[i].nationality].standardOfLiving * 10))) / 1000);

    pCAT += strength + intelligence + education + creativity + religiosity + entrepreneurialism + money + stress + health;
    return pCAT;
  }

  // what stats a person could potentially have in ideal environment
  int idealTotal(int i) {
    int pCAT = 0;
    int strength = 0, intelligence = 0, education = 0, creativity = 0, religiosity = 0, entrepreneurialism = 0, money = 0, stress = 0, health = 0;

    strength = round((100 + 100 * 100 * 10) / 1000);
    intelligence = 100 + 10 * 100;
    education = round((100 + 100 * 100 * 100) / 10000);
    creativity = 100 + 100 * 50;
    religiosity = 100 + 300 * 100;
    entrepreneurialism = round((100 + 100 * 100 * 100) / 10000);
    money = round((100 + 500 * 50 * 100 * 150 * 200 * 200) / 1000000000);
    stress = round((100 + 50 * 100 * 100 * 200 * 200) / 100000000);
    health = round((100 + 200 * 1000) / 1000);

    pCAT += strength + intelligence + education + creativity + religiosity + entrepreneurialism + money + stress + health;
    return pCAT;
  }
  
}

