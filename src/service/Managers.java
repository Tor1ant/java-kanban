package service;


final class Managers {

    static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
