package org.apache.olingo.jpa.processor.core.filter;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.jpa.processor.core.util.IntegrationTestHelper;
import org.apache.olingo.jpa.processor.core.util.TestBase;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ArrayNode;

public class TestJPAQueryWhereClause extends TestBase {

  @Test
  public void testFilterOneEquals() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper("Organizations?$filter=ID eq '3'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
    assertEquals("3", orgs.get(0).get("ID").asText());
  }

  @Test
  public void testFilterOneEqualsTwoProperties() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=DivisionCode eq CountryCode");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(3, orgs.size());
  }

  @Test
  public void testFilterOneEqualsInvert() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper("Organizations?$filter='3' eq ID");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
    assertEquals("3", orgs.get(0).get("ID").asText());
  }

  @Test
  public void testFilterOneNotEqual() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper("Organizations?$filter=ID ne '3'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(9, orgs.size());
  }

  @Test
  public void testFilterOneGreaterEqualsString() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper("Organizations?$filter=ID ge '5'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(5, orgs.size()); // '10' is smaller than '5' when comparing strings!
  }

  @Test
  public void testFilterOneLowerThanTwoProperties() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=DivisionCode lt CountryCode");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(96, orgs.size());
  }

  @Test
  public void testFilterOneGreaterThanString() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper("Organizations?$filter=ID gt '5'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(4, orgs.size()); // '10' is smaller than '5' when comparing strings!
  }

  @Test
  public void testFilterOneLowerThanString() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper("Organizations?$filter=ID lt '5'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(5, orgs.size());
  }

  @Test
  public void testFilterOneLowerEqualsString() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper("Organizations?$filter=ID le '5'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(6, orgs.size());
  }

  @Test
  public void testFilterOneGreaterEqualsNumber() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper("AdministrativeDivisions?$filter=Area ge 119330610");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(2, orgs.size());
  }

  @Test
  public void testFilterOneAndEquals() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=CodePublisher eq 'Eurostat' and CodeID eq 'NUTS2'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(11, orgs.size());
  }

  @Test
  public void testFilterOneOrEquals() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Organizations?$filter=ID eq '5' or ID eq '10'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(2, orgs.size());
  }

  @Test
  public void testFilterOneNotLower() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=not (Area lt 50000000)");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(5, orgs.size());
  }

  @Test
  public void testFilterTwoAndEquals() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=CodePublisher eq 'Eurostat' and CodeID eq 'NUTS2' and DivisionCode eq 'BE25'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
    assertEquals("BEL", orgs.get(0).get("CountryCode").asText());
  }

  @Test
  public void testFilterAndOrEqualsParenthesis() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=CodePublisher eq 'Eurostat' and (DivisionCode eq 'BE25' or  DivisionCode eq 'BE24')&$orderby=DivisionCode desc");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(2, orgs.size());
    assertEquals("BE25", orgs.get(0).get("DivisionCode").asText());
  }

  @Test
  public void testFilterAndOrEqualsNoParenthesis() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=CodePublisher eq 'Eurostat' and DivisionCode eq 'BE25' or  CodeID eq '3166-1'&$orderby=DivisionCode desc");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(4, orgs.size());
    assertEquals("USA", orgs.get(0).get("DivisionCode").asText());
  }

  @Test
  public void testFilterAddGreater() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=Area add 7000000 ge 50000000");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(6, orgs.size());
  }

  @Test
  public void testFilterSubGreater() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=Area sub 7000000 ge 60000000");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(4, orgs.size());
  }

  @Test
  public void testFilterDivGreater() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=Area gt 0 and Area div Population ge 6000");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(3, orgs.size());
  }

  @Test
  public void testFilterMulGreater() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=Area mul Population gt 0");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(8, orgs.size());
  }

  @Test
  public void testFilterMod() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=Area gt 0 and Area mod 3578335 eq 0");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
  }

  @Test
  public void testFilterLength() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Regions?$filter=length(Name) eq 10");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(7, orgs.size());
  }

  @Test
  public void testFilterNow() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Persons?$filter=AdministrativeInformation/Created/At lt now()");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
  }

  @Test
  public void testFilterContains() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=contains(CodeID, '166')");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(33, orgs.size());
  }

  @Test
  public void testFilterEndswith() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=endswith(CodeID, '166-1')");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(3, orgs.size());
  }

  @Test
  public void testFilterStartswith() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=startswith(DivisionCode, 'DE-')");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(16, orgs.size());
  }

  @Test
  public void testFilterIndexOf() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=indexof(DivisionCode,'3') eq 4");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(7, orgs.size());
  }

  @Test
  public void testFilterSubstringStartIndex() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Regions?$filter=Language eq 'de' and substring(Name,7) eq 'Dakota'");
    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(2, orgs.size());
  }

  @Test
  public void testFilterSubstringStartEndIndex() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Regions?$filter=Language eq 'de' and substring(Name,0,5) eq 'North'");

    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(2, orgs.size());
  }

  @Test
  public void testFilterSubstringToLower() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Regions?$filter=Language eq 'de' and tolower(Name) eq 'brandenburg'");

    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
  }

  @Test
  public void testFilterSubstringToUpper() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Regions?$filter=Language eq 'de' and toupper(Name) eq 'HESSEN'");

    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
  }

  @Ignore
  @Test
  public void testFilterSubstringToUpperInvers() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "AdministrativeDivisions?$filter=toupper('nuts1') eq CodeID");

    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
  }

  @Test
  public void testFilterTrim() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Regions?$filter=Language eq 'de' and trim(Name) eq 'Sachsen'");

    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
  }

  @Test
  public void testFilterConcat() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Persons?$filter=concat(concat(LastName, ','), FirstName) eq 'Mustermann,Max'");

    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(1, orgs.size());
  }
//  concat

  @Ignore // TODO check with Olingo
  @Test
  public void testFilterNavigationPropertyValue() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "/Organizations?$filter=Roles/RoleCategory eq 'C'");

    helper.assertStatus(200);
    ArrayNode orgs = helper.getValues();
    assertEquals(2, orgs.size());
  }

  @Test
  public void testFilterSubstringStartEndIndexToLower() throws IOException, ODataException {

    IntegrationTestHelper helper = new IntegrationTestHelper(
        "Regions?$filter=Language eq 'de' and tolower(substring(Name,0,5)) eq 'north'");

    helper.assertStatus(200);

    ArrayNode orgs = helper.getValues();
    assertEquals(2, orgs.size());
  }

}