package pw.aru.core.input

import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import pw.aru.utils.extensions.classOf
import java.util.concurrent.TimeUnit

abstract class AsyncInput protected constructor(private val eventWaiter: EventWaiter, private val timeout: Long, private val unit: TimeUnit) {

    protected abstract fun call(event: GuildMessageReceivedEvent)

    protected abstract fun filter(event: GuildMessageReceivedEvent): Boolean

    protected abstract fun timeout()

    protected fun waitForNextEvent() {
        eventWaiter.waitForEvent(classOf<GuildMessageReceivedEvent>(), ::filter, ::call, timeout, unit, ::timeout)
    }
}

abstract class AsyncCommandInput protected constructor(eventWaiter: EventWaiter, timeout: Long, unit: TimeUnit) : AsyncInput(eventWaiter, timeout, unit) {
    override fun call(event: GuildMessageReceivedEvent) {
        val parts = event.message.contentRaw.split(' ')
        onCommand(event, parts[0], parts.getOrNull(1) ?: "")
    }

    protected abstract fun onCommand(event: GuildMessageReceivedEvent, command: String, args: String)
}