package me.bemayor.components;

public abstract class ComponentMember {
    protected final ComponentManagement manager;

    public ComponentMember(ComponentManagement componentManagement) { manager=componentManagement; }
    public ComponentManagement getManager() { return manager; }

    public abstract void registry();
}
