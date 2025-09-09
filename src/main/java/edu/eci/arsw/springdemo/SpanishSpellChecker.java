package edu.eci.arsw.springdemo;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class SpanishSpellChecker implements SpellChecker {

	@Override
	public String checkSpell(String text) {
		return "revisando ("+text+") con el verificador de sintaxis del espanol";

	}

}
