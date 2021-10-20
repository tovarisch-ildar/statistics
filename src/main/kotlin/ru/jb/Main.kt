package ru.jb

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import ru.jb.scrap.Scraper

fun main() {

    val userName = "admin"
    val password = "123"
    val status = "Submitted"
    val webDriverPath = "C:\\Users\\Leo\\IdeaProjects\\statistics\\src\\main\\resources\\chromedriver.exe"


    System.setProperty(
        "webdriver.chrome.driver",
        webDriverPath
    )

    val options = ChromeOptions()
    options.addArguments("--window-size=1920,1080")
    val driver = ChromeDriver(options)
    try {
        val statistics = Scraper(driver, userName, password).getStatisticForStatus(status)
        System.out.format("%8s%64s%64s%18s", "id", "summary", "description", "comments")
        statistics.tasks.forEach{
            println()
            System.out.format("%8s%64s%64s%18d", it.id, it.summary, it.description, it.commentsCount)
        }
        println("")
        println("Total: ${statistics.total}")
    } finally {
        driver.close()
    }
}