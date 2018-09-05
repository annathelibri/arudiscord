package pw.aru.commands.actions.v2.providers

import pw.aru.commands.actions.v2.Image
import pw.aru.commands.actions.v2.ImageProvider
import pw.aru.utils.caches.URLCache
import pw.aru.utils.extensions.random
import java.io.File

class LocalFileProvider(val files: List<String>) : ImageProvider {
    override fun provide(): Image {
        val file = File(files.random())
        return Image(file.name, file::inputStream)
    }
}

class RandomURLProvider(private val cache: URLCache, val images: List<String>) : ImageProvider {
    override fun provide(): Image {
        val url = images.random()
        return Image(url.substring(url.lastIndexOf('/') + 1)) {
            cache.cacheToFile(url).inputStream()
        }
    }
}