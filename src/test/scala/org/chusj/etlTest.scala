package org.chusj

import java.util
import java.util.{ArrayList, Arrays, List}

import org.chusj.PatientHelper.loadPedigree
import org.scalatest.FunSuite

import collection.JavaConverters._
import scala.collection.mutable

class etlTest extends FunSuite {

  val genotypesFamily0: util.List[String] = new util.ArrayList[String](util.Arrays.asList("1/1", "0/0", "./.")) //AR-IMPOSSIBLE
  val genotypesFamily1: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/1", "0/0", "0/0")) //denovo et AD
  val genotypesFamily2: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/1", "./.", "0/0")) //DeNovo et possible AD
  val genotypesFamily3: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/0", "1/0", "0/0")) //Aucune transmission (n'est pas reporté)
  val genotypesFamily4: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/0", "0/0", "1/0")) //Aucune transmission (n'est pas reporté)
  val genotypesFamily5: util.List[String] = new util.ArrayList[String](util.Arrays.asList("1/0", "./.", "./.")) //DeNovo et possible AD
  val genotypesFamily6: util.List[String] = new util.ArrayList[String](util.Arrays.asList("1/1", "0/0", "0/0")) //AR-IMPOSSIBLE
  val genotypesFamily7: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/1", "1/0", "1/0")) //AD
  val genotypesFamily8: util.List[String] = new util.ArrayList[String](util.Arrays.asList("1/1", "1/0", "1/0")) //AR
  val genotypesFamily9: util.List[String] = new util.ArrayList[String](util.Arrays.asList("1/1", "0/1", "./.")) //possible AR
  val genotypesFamilyA: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/1", "0/1", "0/0")) //AD
  val genotypesFamilyB: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/1", "0/1", "./.")) //AD
  val genotypesFamilyC: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/1", "1/1", "0/1")) //AD
  val genotypesFamilyD: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/1", "0/0", "1/1")) //AD
  val genotypesFamilyE: util.List[String] = new util.ArrayList[String](util.Arrays.asList("1/1", "1/1", "0/1")) //AR
  val genotypesFamilyF: util.List[String] = new util.ArrayList[String](util.Arrays.asList("1/1", "./.", "./.")) //possible AR
  val genotypesFamilyG: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/1", "0/0", "./.")) //DeNovo et possible AD
  val genotypesFamilyH: util.List[String] = new util.ArrayList[String](util.Arrays.asList("0/1", "0/0", "1/0")) //AD
  var isAffectedStatusConsidered = true; // default mode

  test("Testing autosomal dominant transmission with a Solo") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest0.ped")
    // FA0002 SP00011 0 0 1 2
    isAffectedStatusConsidered = true;
    assert(!testAutosomalDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(testAutosomalDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
    assert(testAutosomalDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(!testAutosomalDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(!testAutosomalDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(testAutosomalDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
    assert(!testAutosomalDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
    assert(testAutosomalDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
    assert(!testAutosomalDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
    assert(!testAutosomalDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
    assert(testAutosomalDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
    assert(testAutosomalDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
    assert(testAutosomalDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
    assert(testAutosomalDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
    assert(!testAutosomalDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
    assert(!testAutosomalDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
    assert(testAutosomalDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(testAutosomalDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
  }

  test("Testing autosomal dominant transmission with 0 affected parents") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest1.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 1
    FA0002 SP00036 0 0 2 1
    2,2b,
     */
    isAffectedStatusConsidered = true; // strictMode
    assert(!testAutosomalDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(testAutosomalDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
    assert(testAutosomalDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(!testAutosomalDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(!testAutosomalDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(testAutosomalDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
    assert(!testAutosomalDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
    assert(!testAutosomalDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
    assert(!testAutosomalDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
    assert(!testAutosomalDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
    assert(!testAutosomalDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
    assert(!testAutosomalDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
    assert(!testAutosomalDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
    assert(!testAutosomalDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
    assert(!testAutosomalDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
    assert(!testAutosomalDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
    assert(testAutosomalDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(!testAutosomalDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
    isAffectedStatusConsidered = false; // strictMode
    assert(!testAutosomalDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(testAutosomalDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
    assert(testAutosomalDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(!testAutosomalDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(testAutosomalDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
    assert(testAutosomalDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
    assert(testAutosomalDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
    assert(testAutosomalDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
    assert(testAutosomalDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
    assert(testAutosomalDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(testAutosomalDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
  }

  test("Testing autosomal dominant transmission with affected father @ pos 1") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest2.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 1
     */
    isAffectedStatusConsidered = true; // strictMode
      assert(!testAutosomalDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(!testAutosomalDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
      assert(testAutosomalDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(testAutosomalDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(!testAutosomalDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
      assert(!testAutosomalDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
      assert(!testAutosomalDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
      assert(testAutosomalDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
      assert(testAutosomalDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
      assert(!testAutosomalDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
      assert(!testAutosomalDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
      assert(!testAutosomalDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
      assert(!testAutosomalDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
      assert(!testAutosomalDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
    isAffectedStatusConsidered = false;
      assert(!testAutosomalDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(testAutosomalDominant(genotypesFamily1, pedigrees))  // "0/1", "0/0", "0/0" //denovo et AD
      assert(testAutosomalDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamily3, pedigrees))  // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(testAutosomalDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(testAutosomalDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
      assert(!testAutosomalDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
      assert(!testAutosomalDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
      assert(testAutosomalDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
      assert(testAutosomalDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
      assert(testAutosomalDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
      assert(testAutosomalDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
      assert(!testAutosomalDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
      assert(!testAutosomalDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
      assert(testAutosomalDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(testAutosomalDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
  }

  test("Testing autosomal dominant transmission with 2 affected parents") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest3.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 HP:0000268
     */
    isAffectedStatusConsidered = true; // strictMode
      assert(!testAutosomalDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(!testAutosomalDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
      assert(!testAutosomalDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(testAutosomalDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(testAutosomalDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
      assert(!testAutosomalDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
      assert(!testAutosomalDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
      assert(!testAutosomalDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
      assert(testAutosomalDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
      assert(!testAutosomalDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
      assert(!testAutosomalDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
      assert(!testAutosomalDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
      assert(!testAutosomalDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
      assert(!testAutosomalDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
    isAffectedStatusConsidered = false;
      assert(!testAutosomalDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(testAutosomalDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
      assert(testAutosomalDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(testAutosomalDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
      assert(!testAutosomalDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(testAutosomalDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
      assert(!testAutosomalDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
      assert(!testAutosomalDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
      assert(testAutosomalDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
      assert(testAutosomalDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
      assert(testAutosomalDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
      assert(testAutosomalDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
      assert(!testAutosomalDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
      assert(!testAutosomalDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
      assert(testAutosomalDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(testAutosomalDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
  }

  test("Testing autosomal recessif transmission with a solo") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest0.ped")
    // FA0002 SP00011 0 0 1 2
    isAffectedStatusConsidered = true;
    assert(testAutosomalRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(!testAutosomalRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
    assert(!testAutosomalRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(!testAutosomalRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(!testAutosomalRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(!testAutosomalRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
    assert(testAutosomalRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
    assert(!testAutosomalRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
    assert(testAutosomalRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
    assert(testAutosomalRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
    assert(!testAutosomalRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
    assert(!testAutosomalRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
    assert(!testAutosomalRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
    assert(!testAutosomalRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
    assert(testAutosomalRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
    assert(testAutosomalRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
    assert(!testAutosomalRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(!testAutosomalRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
  }

  test("Testing autosomal recessif transmission with 0 affected parents") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest1.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 1
    FA0002 SP00036 0 0 2 1
     */
    isAffectedStatusConsidered = true // strictMode
    assert(!testAutosomalRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(!testAutosomalRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
    assert(!testAutosomalRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(!testAutosomalRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(!testAutosomalRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(!testAutosomalRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
    assert(!testAutosomalRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
    assert(!testAutosomalRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
    assert(testAutosomalRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
    assert(testAutosomalRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
    assert(!testAutosomalRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
    assert(!testAutosomalRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
    assert(!testAutosomalRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
    assert(!testAutosomalRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
    assert(!testAutosomalRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
    assert(testAutosomalRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
    assert(!testAutosomalRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(!testAutosomalRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
    isAffectedStatusConsidered = false
    assert(testAutosomalRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(!testAutosomalRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
    assert(!testAutosomalRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(!testAutosomalRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(!testAutosomalRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(!testAutosomalRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
    assert(testAutosomalRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
    assert(!testAutosomalRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
    assert(testAutosomalRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
    assert(testAutosomalRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
    assert(!testAutosomalRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
    assert(!testAutosomalRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
    assert(!testAutosomalRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
    assert(!testAutosomalRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
    assert(testAutosomalRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
    assert(testAutosomalRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
    assert(!testAutosomalRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(!testAutosomalRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
  }

  test("Testing autosomal recessif transmission with affected father @ pos 1") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest2.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 1
     */
    isAffectedStatusConsidered = true // strictMode
      assert(!testAutosomalRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(!testAutosomalRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
      assert(!testAutosomalRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(!testAutosomalRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
      assert(!testAutosomalRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
      assert(!testAutosomalRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
      assert(!testAutosomalRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
      assert(!testAutosomalRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
      assert(!testAutosomalRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
      assert(!testAutosomalRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
      assert(!testAutosomalRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
      assert(!testAutosomalRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
      assert(!testAutosomalRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
    isAffectedStatusConsidered = false
      assert(testAutosomalRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(!testAutosomalRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
      assert(!testAutosomalRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
      assert(testAutosomalRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(!testAutosomalRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
      assert(testAutosomalRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
      assert(testAutosomalRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
      assert(!testAutosomalRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
      assert(!testAutosomalRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
      assert(!testAutosomalRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
      assert(!testAutosomalRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
      assert(testAutosomalRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
      assert(testAutosomalRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
      assert(!testAutosomalRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
  }

  test("Testing autosomal recessif transmission with 2 affected parents") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest3.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 HP:0000268
     */

    isAffectedStatusConsidered = true // strictMode
      assert(!testAutosomalRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(!testAutosomalRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
      assert(!testAutosomalRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(!testAutosomalRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
      assert(!testAutosomalRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
      assert(!testAutosomalRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
      assert(!testAutosomalRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
      assert(!testAutosomalRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
      assert(!testAutosomalRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
      assert(!testAutosomalRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
      assert(!testAutosomalRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
      assert(!testAutosomalRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
      assert(!testAutosomalRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
    isAffectedStatusConsidered = false // strictMode
      assert(testAutosomalRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(!testAutosomalRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0" //denovo et AD
      assert(!testAutosomalRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(!testAutosomalRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./." //DeNovo et possible AD
      assert(testAutosomalRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(!testAutosomalRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0" //AD
      assert(testAutosomalRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0" //AR
      assert(testAutosomalRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./." //possible AR
      assert(!testAutosomalRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0" //AD
      assert(!testAutosomalRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./." //AD
      assert(!testAutosomalRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1" //AD
      assert(!testAutosomalRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1" //AD
      assert(testAutosomalRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1" //AR
      assert(testAutosomalRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./." //possible AR
      assert(!testAutosomalRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(!testAutosomalRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0" //AD
  }

  test("Testing denovo transmission with a Solo") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest0.ped")
    // FA0002 SP00011 0 0 1 2
    isAffectedStatusConsidered = true // strictMode
    assert(testDenovo(genotypesFamily0, pedigrees) == "NO") // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(testDenovo(genotypesFamily1, pedigrees) == "NO") // "0/1", "0/0", "0/0" //denovo et AD
    assert(testDenovo(genotypesFamily2, pedigrees) == "NO") // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(testDenovo(genotypesFamily3, pedigrees) == "NO") // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily4, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily5, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily6, pedigrees) == "NO") // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
    assert(testDenovo(genotypesFamily7, pedigrees) == "NO") // "0/1", "1/0", "1/0" //AD
    assert(testDenovo(genotypesFamily8, pedigrees) == "NO") // "1/1", "1/0", "1/0" //AR
    assert(testDenovo(genotypesFamily9, pedigrees) == "NO") // "1/1", "0/1", "./." //possible AR
    assert(testDenovo(genotypesFamilyA, pedigrees) == "NO") // "0/1", "0/1", "0/0" //AD
    assert(testDenovo(genotypesFamilyB, pedigrees) == "NO") // "0/1", "0/1", "./." //AD
    assert(testDenovo(genotypesFamilyC, pedigrees) == "NO") // "0/1", "1/1", "0/1" //AD
    assert(testDenovo(genotypesFamilyD, pedigrees) == "NO") // "0/1", "0/0", "1/1" //AD
    assert(testDenovo(genotypesFamilyE, pedigrees) == "NO") // "1/1", "1/1", "0/1" //AR
    assert(testDenovo(genotypesFamilyF, pedigrees) == "NO") // "1/1", "./.", "./." //possible AR
    assert(testDenovo(genotypesFamilyG, pedigrees) == "NO") // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(testDenovo(genotypesFamilyH, pedigrees) == "NO") // "0/1", "0/0", "1/0" // AD
    isAffectedStatusConsidered = false // strictMode
    assert(testDenovo(genotypesFamily0, pedigrees) == "NO") // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(testDenovo(genotypesFamily1, pedigrees) == "NO") // "0/1", "0/0", "0/0" //denovo et AD
    assert(testDenovo(genotypesFamily2, pedigrees) == "NO") // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(testDenovo(genotypesFamily3, pedigrees) == "NO") // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily4, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily5, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily6, pedigrees) == "NO") // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
    assert(testDenovo(genotypesFamily7, pedigrees) == "NO") // "0/1", "1/0", "1/0" //AD
    assert(testDenovo(genotypesFamily8, pedigrees) == "NO") // "1/1", "1/0", "1/0" //AR
    assert(testDenovo(genotypesFamily9, pedigrees) == "NO") // "1/1", "0/1", "./." //possible AR
    assert(testDenovo(genotypesFamilyA, pedigrees) == "NO") // "0/1", "0/1", "0/0" //AD
    assert(testDenovo(genotypesFamilyB, pedigrees) == "NO") // "0/1", "0/1", "./." //AD
    assert(testDenovo(genotypesFamilyC, pedigrees) == "NO") // "0/1", "1/1", "0/1" //AD
    assert(testDenovo(genotypesFamilyD, pedigrees) == "NO") // "0/1", "0/0", "1/1" //AD
    assert(testDenovo(genotypesFamilyE, pedigrees) == "NO") // "1/1", "1/1", "0/1" //AR
    assert(testDenovo(genotypesFamilyF, pedigrees) == "NO") // "1/1", "./.", "./." //possible AR
    assert(testDenovo(genotypesFamilyG, pedigrees) == "NO") // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(testDenovo(genotypesFamilyH, pedigrees) == "NO") // "0/1", "0/0", "1/0" // AD
  }

  test("Testing denovo transmission with 0 affected parents") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest1.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 1
    FA0002 SP00036 0 0 2 1
     */
    isAffectedStatusConsidered = true // strictMode
    assert(testDenovo(genotypesFamily0, pedigrees) == "NO") // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(testDenovo(genotypesFamily1, pedigrees) == "DeNovo") // "0/1", "0/0", "0/0" //denovo et AD
    assert(testDenovo(genotypesFamily2, pedigrees)== "DeNovo") // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(testDenovo(genotypesFamily3, pedigrees) == "NO") // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily4, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily5, pedigrees)== "DeNovo") // "1/0", "./.", "./." //DeNovo et possible AD
    assert(testDenovo(genotypesFamily6, pedigrees) == "NO") // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
    assert(testDenovo(genotypesFamily7, pedigrees) == "NO") // "0/1", "1/0", "1/0" //AD
    assert(testDenovo(genotypesFamily8, pedigrees) == "NO") // "1/1", "1/0", "1/0" //AR
    assert(testDenovo(genotypesFamily9, pedigrees) == "NO") // "1/1", "0/1", "./." //possible AR
    assert(testDenovo(genotypesFamilyA, pedigrees) == "NO") // "0/1", "0/1", "0/0" //AD
    assert(testDenovo(genotypesFamilyB, pedigrees) == "NO") // "0/1", "0/1", "./." //AD
    assert(testDenovo(genotypesFamilyC, pedigrees) == "NO") // "0/1", "1/1", "0/1" //AD
    assert(testDenovo(genotypesFamilyD, pedigrees) == "NO") // "0/1", "0/0", "1/1" //AD
    assert(testDenovo(genotypesFamilyE, pedigrees) == "NO") // "1/1", "1/1", "0/1" //AR
    assert(testDenovo(genotypesFamilyF, pedigrees) == "NO") // "1/1", "./.", "./." //possible AR
    assert(testDenovo(genotypesFamilyG, pedigrees) == "DeNovo") // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(testDenovo(genotypesFamilyH, pedigrees) == "NO") // "0/1", "0/0", "1/0" // AD
    isAffectedStatusConsidered = false
    assert(testDenovo(genotypesFamily0, pedigrees) == "NO") // "1/1", "0/0", "./." //AR-IMPOSSIBLE
    assert(testDenovo(genotypesFamily1, pedigrees) == "DeNovo") // "0/1", "0/0", "0/0" //denovo et AD
    assert(testDenovo(genotypesFamily2, pedigrees)== "DeNovo") // "0/1", "./.", "0/0" //DeNovo et possible AD
    assert(testDenovo(genotypesFamily3, pedigrees) == "NO") // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily4, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
    assert(testDenovo(genotypesFamily5, pedigrees)== "DeNovo") // "1/0", "./.", "./." //DeNovo et possible AD
    assert(testDenovo(genotypesFamily6, pedigrees) == "NO") // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
    assert(testDenovo(genotypesFamily7, pedigrees) == "NO") // "0/1", "1/0", "1/0" //AD
    assert(testDenovo(genotypesFamily8, pedigrees) == "NO") // "1/1", "1/0", "1/0" //AR
    assert(testDenovo(genotypesFamily9, pedigrees) == "NO") // "1/1", "0/1", "./." //possible AR
    assert(testDenovo(genotypesFamilyA, pedigrees) == "NO") // "0/1", "0/1", "0/0" //AD
    assert(testDenovo(genotypesFamilyB, pedigrees) == "NO") // "0/1", "0/1", "./." //AD
    assert(testDenovo(genotypesFamilyC, pedigrees) == "NO") // "0/1", "1/1", "0/1" //AD
    assert(testDenovo(genotypesFamilyD, pedigrees) == "NO") // "0/1", "0/0", "1/1" //AD
    assert(testDenovo(genotypesFamilyE, pedigrees) == "NO") // "1/1", "1/1", "0/1" //AR
    assert(testDenovo(genotypesFamilyF, pedigrees) == "NO") // "1/1", "./.", "./." //possible AR
    assert(testDenovo(genotypesFamilyG, pedigrees) == "DeNovo") // "0/1", "0/0", "./." //DeNovo et possible AD
    assert(testDenovo(genotypesFamilyH, pedigrees) == "NO") // "0/1", "0/0", "1/0" // AD
  }

  test("Testing denovo transmission with affected father @ pos 1") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest2.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 1
     */

    isAffectedStatusConsidered = true // strictMode
      assert(testDenovo(genotypesFamily0, pedigrees) == "NO") // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(testDenovo(genotypesFamily1, pedigrees) == "NO") // "0/1", "0/0", "0/0" //denovo et AD
      assert(testDenovo(genotypesFamily2, pedigrees) == "DeNovo") // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(testDenovo(genotypesFamily3, pedigrees) == "NO") // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(testDenovo(genotypesFamily4, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(testDenovo(genotypesFamily5, pedigrees) == "DeNovo") // "1/0", "./.", "./." //DeNovo et possible AD
      assert(testDenovo(genotypesFamily6, pedigrees) == "NO") // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(testDenovo(genotypesFamily7, pedigrees) == "NO") // "0/1", "1/0", "1/0" //AD
      assert(testDenovo(genotypesFamily8, pedigrees) == "NO") // "1/1", "1/0", "1/0" //AR
      assert(testDenovo(genotypesFamily9, pedigrees) == "NO") // "1/1", "0/1", "./." //possible AR
      assert(testDenovo(genotypesFamilyA, pedigrees) == "NO") // "0/1", "0/1", "0/0" //AD
      assert(testDenovo(genotypesFamilyB, pedigrees) == "NO") // "0/1", "0/1", "./." //AD
      assert(testDenovo(genotypesFamilyC, pedigrees) == "NO") // "0/1", "1/1", "0/1" //AD
      assert(testDenovo(genotypesFamilyD, pedigrees) == "NO") // "0/1", "0/0", "1/1" //AD
      assert(testDenovo(genotypesFamilyE, pedigrees) == "NO") // "1/1", "1/1", "0/1" //AR
      assert(testDenovo(genotypesFamilyF, pedigrees) == "NO") // "1/1", "./.", "./." //possible AR
      assert(testDenovo(genotypesFamilyG, pedigrees) == "NO") // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(testDenovo(genotypesFamilyH, pedigrees) == "NO") // "0/1", "0/0", "1/0" // AD
    isAffectedStatusConsidered = false
      assert(testDenovo(genotypesFamily0, pedigrees) == "NO") // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(testDenovo(genotypesFamily1, pedigrees) == "DeNovo") // "0/1", "0/0", "0/0" //denovo et AD
      assert(testDenovo(genotypesFamily2, pedigrees)== "DeNovo") // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(testDenovo(genotypesFamily3, pedigrees) == "NO") // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(testDenovo(genotypesFamily4, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(testDenovo(genotypesFamily5, pedigrees)== "DeNovo") // "1/0", "./.", "./." //DeNovo et possible AD
      assert(testDenovo(genotypesFamily6, pedigrees) == "NO") // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(testDenovo(genotypesFamily7, pedigrees) == "NO") // "0/1", "1/0", "1/0" //AD
      assert(testDenovo(genotypesFamily8, pedigrees) == "NO") // "1/1", "1/0", "1/0" //AR
      assert(testDenovo(genotypesFamily9, pedigrees) == "NO") // "1/1", "0/1", "./." //possible AR
      assert(testDenovo(genotypesFamilyA, pedigrees) == "NO") // "0/1", "0/1", "0/0" //AD
      assert(testDenovo(genotypesFamilyB, pedigrees) == "NO") // "0/1", "0/1", "./." //AD
      assert(testDenovo(genotypesFamilyC, pedigrees) == "NO") // "0/1", "1/1", "0/1" //AD
      assert(testDenovo(genotypesFamilyD, pedigrees) == "NO") // "0/1", "0/0", "1/1" //AD
      assert(testDenovo(genotypesFamilyE, pedigrees) == "NO") // "1/1", "1/1", "0/1" //AR
      assert(testDenovo(genotypesFamilyF, pedigrees) == "NO") // "1/1", "./.", "./." //possible AR
      assert(testDenovo(genotypesFamilyG, pedigrees) == "DeNovo") // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(testDenovo(genotypesFamilyH, pedigrees) == "NO") // "0/1", "0/0", "1/0" // AD
  }

  test("Testing denovo transmission with 2 affected parents") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest3.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 HP:0000268
     */
  isAffectedStatusConsidered = true // strictMode
      assert(testDenovo(genotypesFamily0, pedigrees) == "NO") // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(testDenovo(genotypesFamily1, pedigrees) == "NO") // "0/1", "0/0", "0/0" //denovo et AD
      assert(testDenovo(genotypesFamily2, pedigrees) == "NO") // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(testDenovo(genotypesFamily3, pedigrees) == "NO") // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(testDenovo(genotypesFamily4, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(testDenovo(genotypesFamily5, pedigrees) == "DeNovo") // "1/0", "./.", "./." //DeNovo et possible AD
      assert(testDenovo(genotypesFamily6, pedigrees) == "NO") // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(testDenovo(genotypesFamily7, pedigrees) == "NO") // "0/1", "1/0", "1/0" //AD
      assert(testDenovo(genotypesFamily8, pedigrees) == "NO") // "1/1", "1/0", "1/0" //AR
      assert(testDenovo(genotypesFamily9, pedigrees) == "NO") // "1/1", "0/1", "./." //possible AR
      assert(testDenovo(genotypesFamilyA, pedigrees) == "NO") // "0/1", "0/1", "0/0" //AD
      assert(testDenovo(genotypesFamilyB, pedigrees) == "NO") // "0/1", "0/1", "./." //AD
      assert(testDenovo(genotypesFamilyC, pedigrees) == "NO") // "0/1", "1/1", "0/1" //AD
      assert(testDenovo(genotypesFamilyD, pedigrees) == "NO") // "0/1", "0/0", "1/1" //AD
      assert(testDenovo(genotypesFamilyE, pedigrees) == "NO") // "1/1", "1/1", "0/1" //AR
      assert(testDenovo(genotypesFamilyF, pedigrees) == "NO") // "1/1", "./.", "./." //possible AR
      assert(testDenovo(genotypesFamilyG, pedigrees) == "NO") // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(testDenovo(genotypesFamilyH, pedigrees) == "NO") // "0/1", "0/0", "1/0" // AD
  isAffectedStatusConsidered = false
      assert(testDenovo(genotypesFamily0, pedigrees) == "NO") // "1/1", "0/0", "./." //AR-IMPOSSIBLE
      assert(testDenovo(genotypesFamily1, pedigrees) == "DeNovo") // "0/1", "0/0", "0/0" //denovo et AD
      assert(testDenovo(genotypesFamily2, pedigrees)== "DeNovo") // "0/1", "./.", "0/0" //DeNovo et possible AD
      assert(testDenovo(genotypesFamily3, pedigrees) == "NO") // "0/0", "1/0", "0/0" //Aucune transmission (n'est pas reporté)
      assert(testDenovo(genotypesFamily4, pedigrees) == "NO") // "0/0", "0/0", "1/0" //Aucune transmission (n'est pas reporté)
      assert(testDenovo(genotypesFamily5, pedigrees)== "DeNovo") // "1/0", "./.", "./." //DeNovo et possible AD
      assert(testDenovo(genotypesFamily6, pedigrees) == "NO") // "1/1", "0/0", "0/0" //AR-IMPOSSIBLE
      assert(testDenovo(genotypesFamily7, pedigrees) == "NO") // "0/1", "1/0", "1/0" //AD
      assert(testDenovo(genotypesFamily8, pedigrees) == "NO") // "1/1", "1/0", "1/0" //AR
      assert(testDenovo(genotypesFamily9, pedigrees) == "NO") // "1/1", "0/1", "./." //possible AR
      assert(testDenovo(genotypesFamilyA, pedigrees) == "NO") // "0/1", "0/1", "0/0" //AD
      assert(testDenovo(genotypesFamilyB, pedigrees) == "NO") // "0/1", "0/1", "./." //AD
      assert(testDenovo(genotypesFamilyC, pedigrees) == "NO") // "0/1", "1/1", "0/1" //AD
      assert(testDenovo(genotypesFamilyD, pedigrees) == "NO") // "0/1", "0/0", "1/1" //AD
      assert(testDenovo(genotypesFamilyE, pedigrees) == "NO") // "1/1", "1/1", "0/1" //AR
      assert(testDenovo(genotypesFamilyF, pedigrees) == "NO") // "1/1", "./.", "./." //possible AR
      assert(testDenovo(genotypesFamilyG, pedigrees) == "DeNovo") // "0/1", "0/0", "./." //DeNovo et possible AD
      assert(testDenovo(genotypesFamilyH, pedigrees) == "NO") // "0/1", "0/0", "1/0" // AD
  }

  test("Testing X-linked Recessif transmission with a male solo") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest0.ped")
    // FA0002 SP00011 0 0 1 2
    // If the pedigree only contains one person then we decide if
    // the person is female then the variant call list must contain one HOM call,
    // else the variant call list must contain a HET or a HOM call.

    isAffectedStatusConsidered = true // strictMode

    assert(testXRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(testXRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(testXRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(testXRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(testXRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(testXRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(testXRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(testXRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(testXRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(testXRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(testXRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(testXRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(testXRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(testXRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(testXRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(testXRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"

  }

  test("Testing X-linked Recessif transmission with a female solo") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest0F.ped")
    // FA0002 SP00011 0 0 2 2
    // If the pedigree only contains one person then we decide if
    // the person is female then the variant call list must contain one HOM call,

    isAffectedStatusConsidered = true // strictMode
    assert(testXRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(!testXRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(testXRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(testXRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(testXRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(testXRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(testXRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"

  }

  test("Testing X-linked Recessif transmission with a male and 0 affected parent") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest1.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 1
    FA0002 SP00036 0 0 2 1
     */

    isAffectedStatusConsidered = true // strictMode
    assert(testXRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(testXRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(testXRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(testXRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(testXRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(testXRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(testXRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(testXRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"

  }

  test("Testing X-linked Recessif transmission with a female proband and 0 affected parent") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest1F.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 2 2
    FA0002 SP00061 0 0 1 1
    FA0002 SP00036 0 0 2 1
     */

    isAffectedStatusConsidered = true // strictMode
    assert(!testXRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(!testXRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }

  test("Testing X-linked Recessif transmission with a male and affected father @ pos 1") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest2.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 1
     */
    isAffectedStatusConsidered = true // strictMode
      assert(!testXRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
      assert(!testXRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
      assert(testXRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
      assert(!testXRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
      assert(!testXRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
      assert(testXRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
      assert(!testXRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
      assert(testXRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
      assert(testXRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
      assert(testXRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
      assert(testXRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
      assert(testXRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
      assert(testXRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
      assert(!testXRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
      assert(testXRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
      assert(testXRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
      assert(!testXRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
      assert(!testXRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }

  test("Testing X-linked Recessif transmission with a female and affected father @ pos 1") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest2F.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 2 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 1
     */
    isAffectedStatusConsidered = true // strictMode
    assert(!testXRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(!testXRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(testXRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(testXRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(testXRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(testXRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }

  test("Testing X-linked Recessif transmission with a female and affected mth @ pos 2") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTestFMth.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 2 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 1
     */
    isAffectedStatusConsidered = true // strictMode
    assert(!testXRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(!testXRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }

  test("Testing X-linked Recessif transmission with a male and both affected parent") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest3.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 HP:0000268
     */
    isAffectedStatusConsidered = true // strictMode
    assert(!testXRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(testXRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(testXRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(testXRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(testXRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"

  }

  test("Testing X-linked Recessif transmission with a female and both affected parents") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest3F.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 2 2
    FA0002 SP00061 0 0 1 2
    FA0002 SP00036 0 0 2 HP:0000268
     */
    isAffectedStatusConsidered = true // strictMode
    assert(!testXRecessif(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXRecessif(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXRecessif(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(!testXRecessif(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXRecessif(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXRecessif(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(testXRecessif(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(testXRecessif(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXRecessif(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXRecessif(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXRecessif(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(testXRecessif(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(testXRecessif(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXRecessif(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXRecessif(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }

  test("Testing X-linked Dominant transmission with a male solo") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest0.ped")
    // FA0002 SP00011 0 0 1 2
    isAffectedStatusConsidered = true
    assert(!testXDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(!testXDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }
  test("Testing X-linked Dominant transmission with a female solo") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest0F.ped")
    // FA0002 SP00011 0 0 2 2
    isAffectedStatusConsidered = true
    assert(!testXDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(testXDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(testXDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(testXDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(testXDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(testXDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(testXDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(testXDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(testXDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(testXDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(testXDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"

  }

  test("Testing X-linked Dominant transmission with a male and 0 affected parent") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest1.ped")
    isAffectedStatusConsidered = true
    assert(!testXDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(!testXDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }

  test("Testing X-linked Dominant transmission with a male and affected mth") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest2M.ped")
    /*
    FA0002 SP00011 SP00061 SP00036 1 2
    FA0002 SP00061 0 0 1 1
    FA0002 SP00036 0 0 2 2
     */
    isAffectedStatusConsidered = true
    assert(!testXDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(!testXDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"

  }

  test("Testing X-linked Dominant transmission with a female and 0 affected parent") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest1F.ped")
    isAffectedStatusConsidered = true
    assert(!testXDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(testXDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(testXDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(testXDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(testXDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }

  test("Testing X-linked Dominant transmission with a male and affected Father") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest2.ped")

    isAffectedStatusConsidered = true
    assert(!testXDominant(genotypesFamily0, pedigrees)) // "1/1", "0/0", "./."
    assert(!testXDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(!testXDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }
  test("Testing X-linked Dominant transmission with a female and affected father") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTest2F.ped")
    isAffectedStatusConsidered = true
    assert(!testXDominant(genotypesFamily0, pedigrees)) //"1/1", "0/0", "./."
    assert(!testXDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(testXDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(testXDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(testXDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(testXDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(!testXDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(!testXDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }

  test("Testing X-linked Dominant transmission with a female and affected mother") {
    val pedigrees = loadPedigree("src/test/resources/pedigreeTestFMth.ped")
    //(“0/1”, “0/1”, “0/0”) -> 	x_linked_dominant [if female proband with affected father and unaffected mother] 
    isAffectedStatusConsidered = true
    assert(!testXDominant(genotypesFamily0, pedigrees)) //"1/1", "0/0", "./."
    assert(!testXDominant(genotypesFamily1, pedigrees)) // "0/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily2, pedigrees)) // "0/1", "./.", "0/0"
    assert(!testXDominant(genotypesFamily3, pedigrees)) // "0/0", "1/0", "0/0"
    assert(!testXDominant(genotypesFamily4, pedigrees)) // "0/0", "0/0", "1/0"
    assert(testXDominant(genotypesFamily5, pedigrees)) // "1/0", "./.", "./."
    assert(!testXDominant(genotypesFamily6, pedigrees)) // "1/1", "0/0", "0/0"
    assert(!testXDominant(genotypesFamily7, pedigrees)) // "0/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily8, pedigrees)) // "1/1", "1/0", "1/0"
    assert(!testXDominant(genotypesFamily9, pedigrees)) // "1/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyA, pedigrees)) // "0/1", "0/1", "0/0"
    assert(!testXDominant(genotypesFamilyB, pedigrees)) // "0/1", "0/1", "./."
    assert(!testXDominant(genotypesFamilyC, pedigrees)) // "0/1", "1/1", "0/1"
    assert(testXDominant(genotypesFamilyD, pedigrees)) // "0/1", "0/0", "1/1"
    assert(!testXDominant(genotypesFamilyE, pedigrees)) // "1/1", "1/1", "0/1"
    assert(!testXDominant(genotypesFamilyF, pedigrees)) // "1/1", "./.", "./."
    assert(testXDominant(genotypesFamilyG, pedigrees)) // "0/1", "0/0", "./."
    assert(testXDominant(genotypesFamilyH, pedigrees)) // "0/1", "0/0", "1/0"
  }

  test("testing transmission with Alex DL data") {
    //("0/1", "0/0", "0/0")) //-> 	de_novo [if both parents unaffected]
    var test: util.List[String] = null
    test = new util.ArrayList[String](util.Arrays.asList("0/1", "0/0", "0/0"))
    isAffectedStatusConsidered = false
    var pedigrees = loadPedigree("src/test/resources/pedigreeTest1.ped")
    assert(testDenovo(test, pedigrees) == "DeNovo")
    isAffectedStatusConsidered = true
    assert(testDenovo(test, pedigrees) == "DeNovo")
    //("0/1", "0/0", "0/1")) // autosomal_dominant [if affected mother and unaffected father]
    test = new util.ArrayList[String](util.Arrays.asList("0/1", "0/0", "0/1"))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest2M.ped")
    isAffectedStatusConsidered = false
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    //("0/1", "0/0", "1/1")) //-> 	autosomal_dominant with HOM mother [if affected mother and unaffected father]
    test = new util.ArrayList[String](util.Arrays.asList("0/1", "0/0", "1/1"))
    isAffectedStatusConsidered = false
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    //assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))

    //("0/1", "0/0", "./.") -> 	possible de_novo [if both parents unaffected]
    //possible autosomal_dominant [if affected mother and unaffected father]
    test = new util.ArrayList[String](util.Arrays.asList("0/1", "0/0", "./."))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest1.ped")
    assert(testDenovo(test, pedigrees) == "DeNovo")
    isAffectedStatusConsidered = true
    assert(testDenovo(test, pedigrees) == "DeNovo")
    pedigrees = loadPedigree("src/test/resources/pedigreeTest2M.ped")
    isAffectedStatusConsidered = false
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))

    //("0/1", "0/1", "0/1") -> 	autosomal_dominant [if both parents affected]
    test = new util.ArrayList[String](util.Arrays.asList("0/1", "0/1", "0/1"))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest3.ped")
    isAffectedStatusConsidered = false
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))

    // ("0/1", "0/1", "./.") -> 	autosomal_dominant [if at least one affected parent]
    test = new util.ArrayList[String](util.Arrays.asList("0/1", "0/1", "./."))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest2M.ped")
    isAffectedStatusConsidered = false
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(!testAutosomalDominant(test, pedigrees)) // unaffected fth is het
    assert(!testAutosomalRecessif(test, pedigrees))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest1.ped")
    isAffectedStatusConsidered = false
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(!testAutosomalDominant(test, pedigrees)) // unaffected fth is het
    assert(!testAutosomalRecessif(test, pedigrees))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest2.ped")
    isAffectedStatusConsidered = false
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest3.ped")
    isAffectedStatusConsidered = false
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(testAutosomalDominant(test, pedigrees))
    assert(!testAutosomalRecessif(test, pedigrees))


    //("1/1", "0/1", "0/1") -> 	autosomal_recessive [if both parents unaffected]
    test = new util.ArrayList[String](util.Arrays.asList("1/1", "0/1", "0/1"))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest1.ped")
    isAffectedStatusConsidered = false
    assert(!testAutosomalDominant(test, pedigrees))
    assert(testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(!testAutosomalDominant(test, pedigrees))
    assert(testAutosomalRecessif(test, pedigrees))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest2.ped")
    isAffectedStatusConsidered = false
    assert(testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(!testAutosomalRecessif(test, pedigrees))
    pedigrees = loadPedigree("src/test/resources/pedigreeTest3.ped")
    isAffectedStatusConsidered = false
    assert(testAutosomalRecessif(test, pedigrees))
    isAffectedStatusConsidered = true
    assert(!testAutosomalRecessif(test, pedigrees))

  }

  test("Testing zygosity") {
    assert(testZygo())
  }

  test("Testing count alternative allele") {
    assert(testCountAltAllele())
  }

  test("Testing count number of allele") {
    assert(testCountNumberOfAllele())
  }

  test("Testing impact score") {
    assert(
      VepHelper.getImpactScore("HIGH") == 4 &&
        VepHelper.getImpactScore("MODIFIER") == 1 &&
        VepHelper.getImpactScore("MODERATE") == 3 &&
        VepHelper.getImpactScore("LOW") == 2 &&
        VepHelper.getImpactScore("high") == 4 &&
        VepHelper.getImpactScore("BOB") == 0 &&
        VepHelper.getImpactScore("") == 0 &&
        VepHelper.getImpactScore(null) == 0)
  }

  def testZygo(): Boolean =
      VepHelper.zygosity("0/0") == "HOM REF" &&
      VepHelper.zygosity("0|0") == "HOM REF" &&
      VepHelper.zygosity("0/1") == "HET" &&
      VepHelper.zygosity("1/2") == "HET" &&
      VepHelper.zygosity("1/1") == "HOM" &&
      VepHelper.zygosity("2/2") == "HOM"

  def testCountAltAllele() : Boolean = {
      VepHelper.countAlternativeAllele("0/0") == 0 &&
      VepHelper.countAlternativeAllele("0|0") == 0 &&
      VepHelper.countAlternativeAllele("0/1") == 1 &&
      VepHelper.countAlternativeAllele("1/0") == 1 &&
      VepHelper.countAlternativeAllele("1/1") == 2 &&
      VepHelper.countAlternativeAllele("1|1") == 2 &&
      VepHelper.countAlternativeAllele("./.") == 0
  }

  def testCountNumberOfAllele() : Boolean = {
    VepHelper.countNumberOfAllele("0/0") == 2 &&
      VepHelper.countNumberOfAllele("0|0") == 2 &&
      VepHelper.countNumberOfAllele("0/1") == 2 &&
      VepHelper.countNumberOfAllele("1/0") == 2 &&
      VepHelper.countNumberOfAllele("1/1") == 2 &&
      VepHelper.countNumberOfAllele("1|1") == 2 &&
      VepHelper.countNumberOfAllele("./.") == 0
  }

  def testAutosomalDominant(genotypesFamily: util.List[String], familyPed: util.List[Pedigree]) : Boolean = {
    VepHelper.isAutosomalDominant(genotypesFamily,familyPed, isAffectedStatusConsidered)
  }

  def testAutosomalRecessif(genotypesFamily: util.List[String], familyPed: util.List[Pedigree]) : Boolean = {
    VepHelper.isAutosomalRecessif(genotypesFamily,familyPed, isAffectedStatusConsidered)
  }

  def testDenovo(genotypesFamily: util.List[String], familyPed: util.List[Pedigree]) : String = {
    VepHelper.isDeNovo(genotypesFamily,familyPed, isAffectedStatusConsidered)
  }

  def testXRecessif(genotypesFamily: util.List[String], familyPed: util.List[Pedigree]) : Boolean = {
    VepHelper.isXRecessif(genotypesFamily,familyPed, isAffectedStatusConsidered)
  }

  def testXDominant(genotypesFamily: util.List[String], familyPed: util.List[Pedigree]) : Boolean = {
    VepHelper.isXDominant(genotypesFamily,familyPed, isAffectedStatusConsidered)
  }
}
