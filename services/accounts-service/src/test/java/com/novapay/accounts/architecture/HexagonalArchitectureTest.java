package com.novapay.accounts.architecture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DisplayName("Arquitetura Hexagonal")
class HexagonalArchitectureTest {

  private static final String BASE_PACKAGE = "com.novapay.accounts";
  private static JavaClasses classes;

  @BeforeAll
  static void importarClasses(){
    classes = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS).importPackages(BASE_PACKAGE);
  }

  @Test
  @DisplayName("dominio nao deve depender de Spring")
  void dominioNaoDeveDependerDeSpring() {
      noClasses()
              .that().resideInAPackage("..domain..")
              .should().dependOnClassesThat()
              .resideInAnyPackage("org.springframework..")
              .check(classes);
  }

  @Test
  @DisplayName("dominio nao deve depender do JPA")
  void dominioNaoDeveDependerDoJpa() {
      noClasses()
              .that().resideInAPackage("..domain..")
              .should().dependOnClassesThat()
              .resideInAnyPackage("jakarta.persistence..")
              .check(classes);
  }

  @Test
  @DisplayName("dominio nao deve depender da infraestrutura")
  void dominioNaoDeveDependerDaInfraestrutura() {
      noClasses()
              .that().resideInAPackage("..domain..")
              .should().dependOnClassesThat()
              .resideInAnyPackage("..infrastructure..")
              .check(classes);
  }

  @Test
  @DisplayName("Application não deve depender da infraestrutura")
  void applicationNaoDeveDependerDaInfraestrutura() {
      noClasses()
              .that().resideInAPackage("..application..")
              .should().dependOnClassesThat()
              .resideInAnyPackage("..infrastructure..")
              .allowEmptyShould(true)
              .check(classes);
  }
}
