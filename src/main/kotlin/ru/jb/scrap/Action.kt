package ru.jb.scrap

import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import ru.jb.model.Task

class Action(private val driver: ChromeDriver) {

    fun getCount(): Int {
        val text = driver.findElementByXPath("//span[@data-test='ring-tooltip issue-list-counter']//span").text
        return Integer.parseInt(text)
    }

    fun login(wait: WebDriverWait, userName: String, password: String) {
        val iFrame = driver.findElementsByXPath("//iframe")[0]
        driver.switchTo().frame(iFrame)
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit'][@data-test='login-button']")))

        driver.findElementByXPath("//input[@id='username']").sendKeys(userName)
        driver.findElementByXPath("//input[@id='password']").sendKeys(password)
        driver.findElementByXPath("//button[@type='submit'][@data-test='login-button']").click()
    }

    fun selectTasksByStatus(wait: WebDriverWait, status: String) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='State']")))
        driver.findElementByXPath("//button[@aria-label='State']").click()
        driver.findElementByXPath("//input[@aria-label='Filter items']").sendKeys(status)

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@title='$status']")))
        driver.findElementByXPath("//span[@title='$status']").click()
    }

    fun handlePage(): List<Task> {
        val result = ArrayList<Task>()

        val totalTasks = driver.findElements(By.xpath("//td[@data-test='ring-table-cell summary']")).size

        for (i in 0 until totalTasks) {
            selectTask(i)
            Thread.sleep(1000)
            val element = extractTask()
            result.add(element)

        }
        return result
    }

    private fun selectTask(i: Int) {
        driver
            .findElementByXPath(".//tbody[@data-test='ring-table-body']")
            .findElements(By.xpath("//td[@data-test='ring-table-cell summary']"))[i].click()
    }

    private fun extractTask(): Task {
        val id =
            driver.findElementByXPath("//div[@data-test='issue-container']//article//div//div//span//div//a[@data-test='ring-link']//span").text
        val summary = driver.findElementByXPath("//h1[@data-test='ticket-summary']").text
        val description = driver.findElementByXPath("//div[@data-test='drop-zone-container']")
            .findElement(By.tagName("p")).text
        val commentsSize = driver.findElementsByXPath("//div[@data-test='comment-content']").size
        return Task(id, summary, description, commentsSize)
    }

}