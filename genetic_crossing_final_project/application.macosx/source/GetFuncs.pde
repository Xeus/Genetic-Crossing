public class GetFuncs {

  GetFuncs() {
  }

  String getNationality(int _getNationality) {
    String _nationality;
    switch (_getNationality) {
    case 0:
      _nationality = "USA";
      break;
    case 1:
      _nationality = "EU";
      break;
    case 2:
      _nationality = "China";
      break;
    case 3:
      _nationality = "South America";
      break;
    case 4:
      _nationality = "Africa";
      break;
    default:
      _nationality = "USA";
    }
    return _nationality;
  }

  String getReligion(int _getReligion) {
    String _religion;
    switch (_getReligion) {
    case 0:
      _religion = "Buddhism";
      break;
    case 1:
      _religion = "Christianity";
      break;
    case 2:
      _religion = "Confucianism";
      break;
    case 3:
      _religion = "Hinduism";
      break;
    case 4:
      _religion = "Islam";
      break;
    case 5:
      _religion = "Judaism";
      break;
    case 6:
      _religion = "Taoism";
      break;
    default:
      _religion = "Islam";
    }
    return _religion;
  }

  String[] getMBTI(int _getMBTI) {
    String[] _mbti = new String[2];
    switch (_getMBTI) {
    case 0:
      _mbti[0] = "INTP";
      _mbti[1] = "Architect";
      break;
    case 1:
      _mbti[0] = "INTJ";
      _mbti[1] = "Mastermind";
      break;
    case 2:
      _mbti[0] = "ENTP";
      _mbti[1] = "Inventor";
      break;
    case 3:
      _mbti[0] = "ENTJ";
      _mbti[1] = "Field Marshall";
      break;
    case 4:
      _mbti[0] = "INFP";
      _mbti[1] = "Healer";
      break;
    case 5:
      _mbti[0] = "INFJ";
      _mbti[1] = "Counselor";
      break;
    case 6:
      _mbti[0] = "ENFP";
      _mbti[1] = "Champion";
      break;
    case 7:
      _mbti[0] = "ENFJ";
      _mbti[1] = "Teacher";
      break;
    case 8:
      _mbti[0] = "ESFP";
      _mbti[1] = "Performer";
      break;
    case 9:
      _mbti[0] = "ESTP";
      _mbti[1] = "Promoter";
      break;
    case 10:
      _mbti[0] = "ISFP";
      _mbti[1] = "Composer";
      break;
    case 11:
      _mbti[0] = "ISTP";
      _mbti[1] = "Crafter";
      break;
    case 12:
      _mbti[0] = "ESFJ";
      _mbti[1] = "Provider";
      break;
    case 13:
      _mbti[0] = "ESTJ";
      _mbti[1] = "Supervisor";
      break;
    case 14:
      _mbti[0] = "ISFJ";
      _mbti[1] = "Protector";
      break;
    case 15:
      _mbti[0] = "ISTJ";
      _mbti[1] = "Inspector";
      break;
    }
    return _mbti;
  }
}

