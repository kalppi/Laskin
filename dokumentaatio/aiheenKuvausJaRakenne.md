# Laskin

**Aihe:** Helppokäyttöinen laskin, jolla pystyy kätevästi laskemaan laskuja näppäinkomentoja hyväksikäyttäen. Laskinta voi myös itse laajentaa omilla skriptattavilla funktioilla.

**Käyttäjät:** Kaikki laskinta tarvitsevat

**Kaikkien käyttäjien toiminnot:**
- merkkien syöttäminen näppäimistöltä
- merkkien syöttäminen painelemalla nappeja hiirellä
- omien funktioiden luominen editorilla

![Luokkakaavio](luokkakaavio.png)

![Merkin syöttäminen](merkinsyotto.png)

**Rakennekuvaus**

Ohjelman ydin on GUIController, joka ottaa vastaan käyttäjän nappien painallukset. Käyttäjän syöttämät merkit ohjataan CalculationString luokan oliolle, joka kuvaa käyttäjän syöttämää laskua. Kun käyttäjä syöttää enterin painalluksen, laskee CalculationString Calculator luokkaa hyväksikäyttäen lausekkeen tuloksen. Calculator luokka käyttää Parser ja InfixToPostfix luokkia muokkaamaan tekstimuotoisen laskun helpommin käsiteltävään muotoon.
