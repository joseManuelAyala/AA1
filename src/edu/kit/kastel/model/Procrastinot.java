package edu.kit.kastel.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The Procrastinot class represents an entity responsible for managing Tasks and taskLists.
 * It provides additional functionality and methods for managing tasks, such as task creation, deletion
 *    and organization.
 * @author ucxug
 * @version 1.0
 */
public class Procrastinot {
    /**
     * The counter for te number of tasks added.
     */
    private int taskAddedNumber;
    /**
     * The list of Tasks managed by the application.
     */
    private final List<Task> tasks;
    /**
     * The list of TaskList managed by the application.
     */
    private final List<TaskList> taskLists;

    /**
     * Constructs a new Procrstinot instance.
     * Initializes the taskAddedNumber to 1 and initializes the taskLists and Task list as empty linked lists.
     */
    public Procrastinot() {
        this.taskAddedNumber = 1;
        this.taskLists = new LinkedList<>();
        this.tasks = new LinkedList<>();
    }

    /**
     * Adds a new task.
     * @param task the task to be added.
     */
    public void addTask(final Task task) {
        this.tasks.add(task);
        this.taskAddedNumber++;
    }

    /**
     * Readds a task to the system.
     * This happends when the task was a subtaks and its been removed from their main task.
     * @param task the task to be readded.
     */
    public void readdTask(final Task task) {
        this.tasks.remove(task);
        this.tasks.add(task);
    }

    /**
     * Returns the current task added number.
     * @return the current task added number.
     */
    public int getTaskAddedNumber() {
        return this.taskAddedNumber;
    }


    /**
     * Adds the given TaskList to the System.
     * @param taskList the list to be added.
     */
    public void addList(final TaskList taskList) {
        this.taskLists.add(taskList);
    }

    /**
     * Returns the Task matching the given id. The task to be returned can not be deleted.
     * @param taskId the id to be checked.
     * @return the task if it matches the id and its not deleted, null otherwise.
     */
    public Task getTask(final int taskId) {
        for (Task task : this.tasks) {
            if (task.getTaskNumber() == taskId && !task.isDeleted()) {
                return task;
            }
        }
        return null;
    }

    /**
     * Adjust the list of task by removing the specified task and then reorganizing the tasks.
     * The task is removed from the current list and then added back. The method also adjust the
     * lists of tasks within task list and re-sorts the tasks.
     * @param task
     */
    public void addjustList(Task task) {
        this.tasks.remove(task);
        //Checks the TaskLists.

        for (TaskList taskList : this.taskLists) {
            taskList.adjustList(task);
        }
        //Checks the tasks.
        for (Task procrastinotTask : this.tasks) {
            procrastinotTask.addjustList(task);
        }
        this.tasks.add(task);
        sort();
    }


    /**
     * Checks wheter a given TasList in the System is named with the given name.
     * @param listName the name to be checked.
     * @return true if a list name matches the given name,  false otherwise.
     */
    public boolean containsTaskList(String listName) {
        for (TaskList taskList : this.taskLists) {
            if (taskList.getName().equals(listName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the deleted task matching the given id. The task to return must be deleted and the id must match the
     * the given id.
     * @param taskId the id to be checked.
     * @return the task if it matches the id and if it is deleted, null otherwise.
     */
    public Task getDeletedTask(final int taskId) {
        for (Task task : this.tasks) {
            if (task.getTaskNumber() == taskId && task.isDeleted()) {
                return task;
            }
        }
        return null;
    }

    /**
     * Returns the taskList matching the given name.
     * @param listName the name to be checked.
     * @return true if the taskList name matches the given name.
     */
    public TaskList getTaskList(final String listName) {
        for (TaskList taskList : this.taskLists) {
            if (listName.equals(taskList.getName())) {
                return taskList;
            }
        }
        return null;
    }

    /**
     * Adds a subtask to a given task. If the task to be added as subtaks is alredy subtask from another task
     *    it will be removed.
     * @param parentTask    the parent task.
     * @param subTask the subtask to be added to the task.
     */
    public void addSubTask(final Task parentTask, final Task subTask) {
        for (Task task : this.tasks) {
            if (task.getSubTasks().contains(subTask)) {
                task.deleteSubTask(subTask);
            }
        }
        parentTask.addSubTask(subTask);
        adjustTasks(subTask);
    }

    /**
     * Returns a list of tasks tagged with the given tag.
     * @param tag the tag to be checked.
     * @return a new list of tasks tagged with the given tag.
     */
    public List<Task> getTaggedTask(final String tag) {
        List<Task> uniqueTaggedTasks = new LinkedList<>();
        for (Task task : this.tasks) {
            if (task.getTags().contains(tag) || task.getTaskListTags().contains(tag)) {
                uniqueTaggedTasks.add(task);
            }
        }
        uniqueTaggedTasks.sort(Task::compareTo);
        sortTaggedTask(uniqueTaggedTasks, tag);
        return new LinkedList<>(uniqueTaggedTasks);
    }

    private void sortTaggedTask(List<Task> taggedTasks, String tag) {
        taggedTasks.sort((task1, task2) -> {
            if (task1.getPriority() != task2.getPriority()) {
                return Integer.compare(task2.getPriority().getPriorityOrder(),
                    task1.getPriority().getPriorityOrder());
            } else if (Integer.compare(task1.getTags().indexOf(tag), task2.getTags().indexOf(tag)) != 0) {
                return Integer.compare(task1.getTags().indexOf(tag), task2.getTags().indexOf(tag));
            }
            return Integer.compare(task1.getTaskNumber(), task2.getTaskNumber());
        });
    }

    /**
     * Sets all the tasks to unprinted.
     */
    public void setUnprinted() {
        for (Task task : this.tasks) {
            task.setUnprinted();
        }
    }

    /**
     * Returns all the task in the Procrastinot System.
     * @return a list of task in the taskAministrator object.
     */
    public List<Task> getTasks() {
        this.tasks.sort(Task::compareTo);
        return new LinkedList<>(this.tasks);
    }

    /**
     * Retrieves a list of tasks id representing duplicates task based on name and dead line.
     * Duplicates task are those that have the same and of the dealines is null or the dead lines are equal.
     * @return A sorted list of task numbers representing duplicates task id.
     */
    public List<Integer> getDuplicates() {
        Set<Integer> duplicateIndices = new HashSet<>();
        for (Task taskOne : this.tasks) {
            for (Task taskTwo : this.tasks) {
                if (taskTwo != taskOne && !taskOne.isDeleted() && !taskTwo.isDeleted()) {
                    boolean equalsName = taskOne.getName().equals(taskTwo.getName());
                    boolean equalsDate = taskOne.getDeadline() ==  null || taskTwo.getDeadline() == null;
                    if ((equalsDate || taskOne.getDeadline().isEqual(taskTwo.getDeadline()))  && equalsName) {
                        duplicateIndices.add(taskOne.getTaskNumber() - 1);
                        duplicateIndices.add(taskTwo.getTaskNumber() - 1);
                    }
                }
            }
        }
        List<Integer> duplicates = new LinkedList<>(duplicateIndices);
        Collections.sort(duplicates);
        return duplicates;
    }

    /**
     * Retrieves a list of task list containing the specified task.
     * @param task the task to search for in the taskLists.
     * @return A list of task list containing the specified task.
     */
    public List<TaskList> getParentList(Task task) {
        List<TaskList> taskParents = new LinkedList<>();
        for (TaskList taskList : this.taskLists) {
            if (taskList.containsTask(task)) {
                taskParents.add(taskList);
            }
        }
        return taskParents;
    }

    /**
     * Retrieves a list of to-do task from the task collection.
     * @return A sorted list of tod0 tasks.
     */
    public List<Task> getTodoTask() {
        sort();
        return new LinkedList<>(this.tasks);

    }
    private void adjustTasks(Task task) {
        for (TaskList taskList : this.taskLists) {
            taskList.adjust(task);
        }
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
