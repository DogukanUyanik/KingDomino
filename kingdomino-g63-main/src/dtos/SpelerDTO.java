package dtos;

import domein.Koning;
import domein.Koninkrijk;
import utils.Kleur;

/**
 * DTO klasse voor een speler.
 * Een SpelerDTO bestaat uit een gebruikersnaam(String), geboortejaar(int), het aantal keren dat deze speler al heeft gewonnen(int), 
 * het aantal keren dat deze speler al heeft gespeeld(int), een aantal prestige punten(int), een kleur(Kleur), een koning(Koning) en een koninkrijk(Koninkrijk)
 */

public record SpelerDTO(String gebruikersnaam, int geboortejaar, int aantalGewonnen, int aantalGespeeld, int prestigePunten, Kleur kleur, Koning koning, Koninkrijk koninkrijk) {
}
