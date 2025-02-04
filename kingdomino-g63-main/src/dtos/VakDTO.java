package dtos;

import utils.Landschapstype;

/**
 * DTO klasse voor een vak
 * Een VakDTO bestaat uit een landschapstype(Landschapstype) en het aantal kronen(int) erop.
 */

public record VakDTO(Landschapstype landschapstype, int kronen) {
    
}