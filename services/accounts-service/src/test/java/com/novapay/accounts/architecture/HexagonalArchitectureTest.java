package com.novapay.accounts.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DisplayName("Arquitetura Hexagonal")
class HexagonalArchitectureTest {

  private static final String BASE_PACKAGE = "com.novapay.accounts";

  private static JavaClasses classes;

  @BeforeAll
  static void importarClasses() {
    classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(BASE_PACKAGE);
  }

  @Test
  @DisplayName("core nao deve depender de adapter")
  void coreNaoDeveDependerDeAdapter() {
    noClasses()
            .that().resideInAPackage("..core..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..adapter..")
            .check(classes);
  }

  @Test
  @DisplayName("core nao deve depender de JPA")
  void coreNaoDeveDependerDeJpa() {
    noClasses()
            .that().resideInAPackage("..core..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("jakarta.persistence..")
            .check(classes);
  }

  @Test
  @DisplayName("model nao deve depender de Spring")
  void modelNaoDeveDependerDeSpring() {
    noClasses()
            .that().resideInAPackage("..core.model..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("org.springframework..")
            .check(classes);
  }
}