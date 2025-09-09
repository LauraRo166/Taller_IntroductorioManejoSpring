# Escuela Colombiana de Ingeniería
# Arquitecturas de Software - ARSW

---
## Integrantes:
- Sergio Andrés Bejarano Rodríguez
- Laura Daniela Rodríguez Sánchez
---

### Taller – Principio de Inversión de dependencias, Contenedores Livianos e Inyección de dependencias.

Parte I. Ejercicio básico.

Para ilustrar el uso del framework Spring, y el ambiente de desarrollo para el uso del mismo a través de Maven (y NetBeans), se hará la configuración de una aplicación de análisis de textos, que hace uso de un verificador gramatical que requiere de un corrector ortográfico. A dicho verificador gramatical se le inyectará, en tiempo de ejecución, el corrector ortográfico que se requiera (por ahora, hay dos disponibles: inglés y español).

*1. Abra el los fuentes del proyecto en NetBeans.*

Abrimos el proyecto en Apache NetBeans:

{Imagen en NetBeans}


*2. Revise el archivo de configuración de Spring ya incluido en el proyecto (src/main/resources). El mismo indica que Spring buscará automáticamente los 'Beans' disponibles en el paquete indicado.*

Observamos que `<context:component-scan>` le indica a Spring que debe escanear automáticamente 
todas las clases dentro del paquete `edu.eci.arsw` y sus subpaquetes. Cualquier clase dentro de 
ese paquete que esté anotada con `@Component`, `@Service`, `@Repository` o `@Controller` será registrada 
como un Bean en el ApplicationContext.

{Imagen applicationContext}

*3. Haciendo uso de la [configuración de Spring basada en anotaciones](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-spring-beans-and-dependency-injection.html) marque con las anotaciones @Autowired y @Service las dependencias que deben inyectarse, y los 'beans' candidatos a ser inyectadas -respectivamente-:*

* GrammarChecker será un bean, que tiene como dependencia algo de tipo 'SpellChecker'.

```java
@Service
public class GrammarChecker {

    @Autowired
	SpellChecker sc;

	String x;
        
        
	public SpellChecker getSpellChecker() {
		return sc;
	}

	public void setSpellChecker(SpellChecker sc) {
		this.sc = sc;
	}


	public String check(String text){
		
		StringBuffer sb=new StringBuffer();
		sb.append("Spell checking output:"+sc.checkSpell(text));
		sb.append("Plagiarism checking output: Not available yet");
		
		
		return sb.toString();
		
	}
}
```

* EnglishSpellChecker y SpanishSpellChecker son los dos posibles candidatos a ser inyectados. Se debe seleccionar uno, u otro, mas NO ambos (habría conflicto de resolución de dependencias). Por ahora haga que se use EnglishSpellChecker.
 
Marcamos `EnglishSpellChecker` y `SpanishSpellChecker` como candidatos:
- **EnglishSpellChecker**
```java
@Component
public class EnglishSpellChecker implements SpellChecker {

	@Override
	public String checkSpell(String text) {		
		return "Checked with english checker:"+text;
	}
    
}
```

- **SpanishSpellChecker**
```java
@Component
public class SpanishSpellChecker implements SpellChecker {

	@Override
	public String checkSpell(String text) {
		return "revisando ("+text+") con el verificador de sintaxis del espanol";
                         
	}

}
```

Tal como indica el enunciado, se debe seleccionar uno u otro, pues hay conflicto de 
resolución de dependencias. Para evitar esto y que por ahora use `EnglishSpellChecker`
en la clase `GrammarChecker` agregamos la etiqueta `@Qualifier("englishSpellChecker")`.
Donde `"englishSpellChecker"` es el nombre del bean, que por defecto será el nombre de 
la clase con la primera letra en minúscula

```java
    @Autowired
    @Qualifier("englishSpellChecker")
	SpellChecker sc;
```

*4.	Haga un programa de prueba, donde se cree una instancia de GrammarChecker mediante Spring, y se haga uso de la misma:*

```java
	public static void main(String[] args) {
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
		GrammarChecker gc=ac.getBean(GrammarChecker.class);
		System.out.println(gc.check("la la la "));
	}
```

Al ejecutar la clase main obtenemos la siguiente salida:

{Salida con english}

*5. Modifique la configuración con anotaciones para que el Bean ‘GrammarChecker‘ ahora haga uso de la clase SpanishSpellChecker (para que a GrammarChecker se le inyecte EnglishSpellChecker en lugar de  SpanishSpellChecker. Verifique el nuevo resultado.*

Modificamos en la clase `GrammarChecker` la etiqueta `@Qualifier("englishSpellChecker")`, cambiando el valor por
`"spanishSpellChecker"`, es decir que nuestra etiqueta ahora será `@Qualifier("spanishSpellChecker")`.
```java
    @Autowired
    @Qualifier("spanishSpellChecker")
	SpellChecker sc;
```

Ejecutamos y obtenemos el siguiente resultado:



