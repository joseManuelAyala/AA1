package edu.kit.kastel.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a collections of Tasks organized into a list. A TaskList can contain Tasks and
 *     provides methods to manage and interact with them. The TaskList can also be tagged with certain tags.
 * @author ucxug
 * @version 1.0
 */

public class TaskList  {
    /**
     * The name of the TaskList.
     */
    private final String name;
    /**
     * The tags of the TaskList.
     */
    private final List<String> tags;
    /**
     * A list of tasks added to the TaskList.
     */
    private  final List<Task> tasks;


    /**
     * Constructs a new Instance of taskList.
     * @param name the name of the task list.
     */
    public TaskList(final String name) {
        this.name = name;
        this.tags = new LinkedList<>();
        this.tasks = new LinkedList<>();
    }

    /**
     * Returns the name of the task list.
     * @return the name of the tag list.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the task list contains a given tag.
     * @param tag the tag to be checked.
     * @return true if the task list contains the tag, false otherwise.
     */
    public boolean containsTag(final String tag) {
        return this.tags.contains(tag);
    }

    /**
     * Adds a given tag to the task list.
     * @param tag the tag to be added.
     */
    public void addTag(final String tag) {
        this.tags.add(tag);
        for (Task task : tasks) {
            //tags all the list tasks with the given tag.
            task.addTag(tag, true);
        }
    }

    /**
     * Checks if the task list contains a given Task.
     * @param task the task to be checked.
     * @return true if the task list contains the given task, false otherwise.
     */
    public boolean containsTask(final Task task) {
        if (this.tasks.contains(task)) {
            return true;
        }
        for (Task listTasks : this.tasks) {
            if (subTaskContainsTask(listTasks, task)) {
                return true;
            }
        }
        return false;
    }


    private boolean subTaskContainsTask(Task parentTask, Task taskToSearch) {
        List<Task> subTasks = parentTask.getSubTasks();
        if (!subTasks.isEmpty() && subTasks.contains(taskToSearch)) {
            return true;
        }
        for (Task task : parentTask.getSubTasks()) {
            if (subTaskContainsTask(task, taskToSearch)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a task to the taskList.
     * @param task the task to be added.
     */
    public void addTask(final Task task) {
        this.tasks.add(task);
    }


    /**
     * Returns all the task of the taskList.
     * @return the list of task  from the taskList.
     */
    public List<Task> getTasks() {
        List<Task>  taggedTask = new LinkedList<>();
        for (Task task : this.tasks) {
            if (!task.isDeleted() && !taggedTask.contains(task)) {
                taggedTask.add(task);
            }
        }
        sort();
        return new LinkedList<>(taggedTask);
    }

    /**
     * Helps to sort a task that has been deleted.
     * @param task the task to be sorted.
     */
    public void adjustList(Task task) {
        if (this.tasks.contains(task)) {
            this.tasks.remove(task);
            this.tasks.add(task);   
        }
    }

    /**
     * Adjust the task list by removing the provided task and its associated subtask recursively,
     * if the task or any of its subtask are found within the current tasks list.
     * @param taskToSearch the Task to be searched for and removed along within subtasks.
     */
    public void adjust(Task taskToSearch) {
        for (Task task : this.tasks) {
            if (isSubtask(taskToSearch, task)) {
                this.tasks.removeAll(Collections.singleton(taskToSearch));
            }
        }
    }

    private boolean isSubtask(Task subtTask, Task parentTask) {
        if (parentTask.getSubTasks().contains(subtTask)) {
            return true;
        }
        for (Task task : parentTask.getSubTasks()) {
            if (isSubtask(subtTask, task)) {
                return true;
            }
        }
        return false;
    }

    private void sort() {
        this.tasks.sort((task1, task2) -> {
            if (task1.getPriority() != task2.getPriority()) {
                return Integer.compare(task2.getPriority().getPriorityOrder(),
                    task1.getPriority().getPriorityOrder());
            }
            return 0;
        });
    }
}
