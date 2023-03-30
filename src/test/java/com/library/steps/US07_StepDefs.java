package com.library.steps;

import com.library.pages.BasePage;
import com.library.pages.BookPage;
import com.library.pages.BorrowedBooksPage;
import com.library.pages.LoginPage;
import com.library.utility.BrowserUtil;
import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.Driver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class US07_StepDefs extends BookPage {

    String bookName1;
    LoginPage loginPage = new LoginPage();

    BorrowedBooksPage borrowBooksPage = new BorrowedBooksPage();


    @Given("the {string} on the home page")
    public void theOnTheHomePage(String login) {
        loginPage.login(login);
    }

    @And("the user navigates to {string} page")
    public void theUserNavigatesToPage(String moduleName) {

        navigateModule(moduleName);


    }

    @And("the user searches for {string} book")
    public void theUserSearchesForBook(String bookName1) {
        search.sendKeys(bookName1);
        this.bookName1 = bookName1;

        BrowserUtil.waitFor(3);

    }

    @When("the user clicks Borrow Book")
    public void theUserClicksBorrowBook() {


        //  List<WebElement> allButtons = Driver.getDriver().findElements(By.xpath("//td[3][.='"+bookName1+ "']/../td/a"));

        // for(WebElement eachButton : allButtons){
        //   if(eachButton.isEnabled()){


        borrowBook().click();


        BrowserUtil.waitFor(3);


    }

    @Then("verify that book is shown in {string} page")
    public void verifyThatBookIsShownInPage(String moduleName) {
        navigateModule(moduleName);

        boolean bookBorrowed = false;

        for (WebElement eachBook : borrowBooksPage.allBorrowedBooksName) {

            if (eachBook.getText().equals(bookName1))

                bookBorrowed = true;


        }
        Assert.assertTrue(bookBorrowed);

        //  BrowserUtil.waitFor(10);

        //  System.out.println(eachBook.getText());

        //  System.out.println(eachBook.getText());
        // System.out.println(eachBook.getAttribute("td[6]"));
    }

    //    WebElement borrowedBook = Driver.getDriver().findElement(By.xpath("//*[contains(text(), 'Self Confidence')]/../td[contains(text(), 'NOT')]"));
    //    Assert.assertTrue(borrowedBook.isDisplayed());

    //   Assert.assertTrue( borrowBooksPage.allBorrowedBooksName.toString().contains(bookName1));

    //  }

    @And("verify logged student has same book in database")
    public void verifyLoggedStudentHasSameBookInDatabase() {
        DB_Util.runQuery("select name,is_returned,email from book_borrow join books b on \n" +
                "    b.id = book_borrow.book_id join users u on u.id = book_borrow.user_id\n" +
                "where name='" + bookName1 + "'\n" +
                "and is_returned='0'" +
                "and email='" + ConfigurationReader.getProperty("student_username") + "'");

        String email = DB_Util.getCellValue(1, "email");
        System.out.println(email);
        Assert.assertEquals(email, ConfigurationReader.getProperty("student_username"));

    }

    @Then("user returns borrowed book")
    public void userReturnsBorrowedBook() {

        WebElement returnBookButton = Driver.getDriver().findElement(By.xpath("//tbody//td[6][contains(text(), 'NOT')]/../td[2][contains(text(), '" + bookName1 + "')]/../td/a"));
    }
}