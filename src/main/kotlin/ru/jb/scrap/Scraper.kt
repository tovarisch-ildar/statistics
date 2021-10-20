package ru.jb.scrap

import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import ru.jb.model.Statistics
import ru.jb.model.Task


class Scraper(private val driver: ChromeDriver, private val userName: String, private val password: String) {

    private val actions = Action(driver)

    fun getStatisticForStatus(status: String): Statistics {

        driver.get("http://localhost:8080/issues")

        val wait = WebDriverWait(driver, 10)

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button'][@data-test='ring-header-login-button']")))
        driver.findElementByXPath("//button[@type='button'][@data-test='ring-header-login-button']").click()

        actions.login(wait, userName, password)
        actions.selectTasksByStatus(wait, status)
        Thread.sleep(1000)
        val count = actions.getCount()
        Thread.sleep(2000)
        val result = ArrayList<Task>()
        while (true) {
            result.addAll(actions.handlePage())
            println("New Page")
            val nextPageButton =
                driver.findElementsByXPath("//button[@data-test='ring-link']//span//span[text()='Next page']")
            if (nextPageButton.isNotEmpty()) {
                nextPageButton[0].click()
                Thread.sleep(1500)
            } else {
                break
            }
        }

        result.sortBy { task -> task.commentsCount }

        val fromIndex = result.size - 10
        val top = result.subList(if (fromIndex > 0) fromIndex else 0, result.size)
        return Statistics(count, top)
    }


}