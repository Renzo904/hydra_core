package knf.hydra.module.jkanime.models

import androidx.room.Entity
import knf.hydra.core.models.DirectoryModel
import knf.hydra.core.models.data.Category
import knf.hydra.core.models.data.LayoutType
import org.jsoup.nodes.Element
import pl.droidsonroids.jspoon.ElementConverter
import pl.droidsonroids.jspoon.annotation.Selector
import kotlin.random.Random


@Entity(tableName = "calendar")
class JkDirectoryModel: DirectoryModel() {
    @Selector("div.col-md-5.custom_thumb2 > a", converter = IdConverter::class)
    override var id: Int = Random.nextInt()

    @Selector("h5.card-title > a")
    override var name: String = ""

    @Selector("div.col-md-5.custom_thumb2 > a", attr = "abs:href")
    override var infoLink: String = ""

    @Selector("img", attr = "abs:src")
    override var imageLink: String? = ""

    override var type: String? = null

    @Selector("div > p.card-txt", converter = CategoryConverter::class)
    override var category: Category = Category.ANIME


    override var infoLayoutType: LayoutType = LayoutType.UNKNOWN

    class IdConverter: ElementConverter<Int> {
        override fun convert(node: Element, selector: Selector): Int {
            val id = node.attr("href").substringBeforeLast("/").substringAfterLast("/")
            return id.hashCode()
        }
    }

    class CategoryConverter: ElementConverter<Category> {
        override fun convert(node: Element, selector: Selector): Category {
            if (node.text().replace(" ", "") == "Pelicula"){
                return Category.MOVIE
            }
            return Category.ANIME
        }
    }

}



class DirectoryPage{
    @Selector("div.row.row-cols-md-3.custom_flex > div")
    var list = emptyList<JkDirectoryModel>()
}