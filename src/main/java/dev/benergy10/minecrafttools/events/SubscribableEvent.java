package dev.benergy10.minecrafttools.events;

import dev.benergy10.minecrafttools.utils.Logging;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class SubscribableEvent<T extends Event, S> {

    private final Class<T> eventClass;
    private final Set<S> subscribers;

    private boolean registered = false;
    private EventPriority priority = EventPriority.NORMAL;
    private boolean ignoreCancelled = false;
    private boolean oneTimeUse = false;
    private Function<T, S> eventTarget;
    private Consumer<T> runner;

    private SubscribableEvent(@NotNull Class<T> eventClass) {
        this.eventClass = eventClass;
        this.subscribers = new HashSet<>();
    }

    public boolean register(@NotNull Plugin plugin) {
        if (this.registered) {
            throw new IllegalArgumentException("Event already registered!");
        }
        Bukkit.getPluginManager().registerEvent(
                this.eventClass,
                new Listener() { },
                this.priority,
                this.createExecutor(),
                plugin,
                this.ignoreCancelled
        );
        this.registered = true;
        return true;
    }

    private @NotNull EventExecutor createExecutor() {
        return (listener, event) -> {
            if (this.subscribers.isEmpty() || !this.eventClass.isInstance(event)) {
                return;
            }
            T eventInstance = (T) event;
            S target = this.eventTarget.apply(eventInstance);
            if (!this.subscribers.contains(target)) {
                return;
            }
            this.runner.accept(eventInstance);
            if (this.oneTimeUse) {
                this.unsubscribe(target);
            }
        };
    }

    public boolean subscribe(@NotNull S s) {
        if (!this.registered) {
            throw new IllegalArgumentException("Register the event before adding subscribers!");
        }
        Logging.debug("Subscribing %s from %s...", s, this.eventClass.getName());
        return this.subscribers.add(s);
    }

    public boolean unsubscribe(@NotNull S s) {
        Logging.debug("Unsubscribing %s from %s...", s, this.eventClass.getName());
        return this.subscribers.remove(s);
    }

    public @NotNull Class<T> getEventClass() {
        return eventClass;
    }

    public boolean isSubscriber(S s) {
        return this.subscribers.contains(s);
    }

    public @NotNull Collection<S> getSubscribers() {
        return subscribers;
    }

    public boolean isRegistered() {
        return registered;
    }

    public @NotNull EventPriority getPriority() {
        return priority;
    }

    public boolean isIgnoreCancelled() {
        return ignoreCancelled;
    }

    public boolean isOneTimeUse() {
        return oneTimeUse;
    }

    public @NotNull Function<T, S> getEventTarget() {
        return eventTarget;
    }

    public @NotNull Consumer<T> getRunner() {
        return runner;
    }

    public static class Creator<T extends Event, S> {

        private final SubscribableEvent<T, S> event;

        public Creator(@NotNull Class<T> eventClass) {
            this.event = new SubscribableEvent<>(eventClass);
        }

        public @NotNull Creator<T, S> priority(@NotNull EventPriority priority) {
            this.event.priority = priority;
            return this;
        }

        public @NotNull Creator<T, S> ignoreCancelled(boolean ignoreCancelled) {
            this.event.ignoreCancelled = ignoreCancelled;
            return this;
        }

        public @NotNull Creator<T, S> oneTimeUse(boolean oneTimeUse) {
            this.event.oneTimeUse = oneTimeUse;
            return this;
        }

        public @NotNull Creator<T, S> eventTarget(@NotNull Function<T, S> eventTarget) {
            this.event.eventTarget = eventTarget;
            return this;
        }

        public @NotNull Creator<T, S> runner(@NotNull Consumer<T> runner) {
            this.event.runner = runner;
            return this;
        }

        public @NotNull SubscribableEvent<T, S> create() {
            Objects.requireNonNull(this.event.eventTarget);
            Objects.requireNonNull(this.event.runner);
            return this.event;
        }

        public @NotNull SubscribableEvent<T, S> register(@NotNull Plugin plugin) {
            final SubscribableEvent<T, S> subscribableEvent = this.create();
            subscribableEvent.register(plugin);
            return subscribableEvent;
        }
    }
}
