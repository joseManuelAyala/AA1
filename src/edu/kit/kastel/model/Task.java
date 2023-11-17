package edu.kit.kastel.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * Represents a task that can have subtasks, priorities, and tags.
 * A task can be compared and sorted based on their priorities, task numbers and added orders.
 * This class implements  the {@link Comparable} interface, allowing task to be compared.
 * @author ucxug
 * @version 1.0
 */
public class Task implements Comparable<Task> {

    private static final String TASK_REPRESENTATION_FORMAT = "- [%s] %s";
    private static final String PRIORITY_REPRESENTATION_FORMAT = " [%s]";
    private static final String TAG_REPRESENTATION_FORMAT = " (%s)";
    private static final String DATE_REPRESENTATION_FORMAT = " --> %s";
    private static final String TOGGLE_CHAR_FORMAT = "x";
    private static final String DATE_TAG_SEPARATOR = ":";
    private static final String TAG_SEPARATOR = ", ";
    private static final String DELIMITER = " ";
    /**
     * Represents the priority of the task.
     */
    private Priority priority;
    /**
     * Indicates whether a task is done or not.
     */
    private boolean done;
    /**
     * The deadline of the task.
     */
    private LocalDate deadline;
    /**
     * The task I/dnumber.
     */
    private final int taskNumber;
    /**
     * The task name.
     */
    private final String name;
    /**
     * Indicates whether the task was deleted.
     */
    private boolean deleted;
    /**
     * The tags, tagging the task.
     */
    private final List<String> tags;

    private final List<String> taskListTags;
    /**
     * Indicates whether the task is already been printed.
     */
    private boolean printed;
    private boolean wasDeleted;
    /**
     * A list containing all the subtask.
     */
    private final List<Task> subTasks;
    /**
     * The Parent task of the task
     */
    private Task parentTask;
    /**
     * The subtask number of the task.
     */
    private int subTaskNumber;
    private int deletedTask;

    /**
     * Constructs a new Task object.
     * @param taskName   the name for the task.
     * @param taskNumber the task number.
     */
    public Task(final String taskName, final int taskNumber) {
        this.taskNumber = taskNumber;
        this.name = taskName;
        this.done = false;
        this.tags = new LinkedList<>();
        this.subTasks = new LinkedList<>();
        this.deleted = false;
        this.parentTask = null;
        this.taskListTags = new LinkedList<>();
        /*The priority will be seted to the default priority, it can later be changed.
        It helps to prevent a nullpointer exception.*/
        this.priority = Priority.ND;
        this.deletedTask = 0;

    }

    /**
     * Sets the priority for the task.
     * If the parameter priority equals null , the priority will be the default priority.
     * @param priority the priority to be seted.
     */
    public void setPriority(final Priority priority) {
        this.priority = Objects.requireNonNullElse(priority, Priority.ND);
    }

    /**
     * Returns the priority of the task.
     * @return the priority of the task.
     */
    public Priority getPriority() {
        return this.priority;
    }


    /**
     * Sets the given taks to done/undone.
     * @param done the done boolean to be set on to the task.
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Checks if the task is done.
     * @return true if the task has is done, false otherwise.
     */
    public boolean isDone() {
        return this.done;
    }

    /**
     * Returns the dead line of the task.
     * @return the dead line of the task.
     */
    public LocalDate getDeadline() {
        return this.deadline;
    }

    /**
     * Sets the dead line for the task.
     * @param deadline the deadline to be set on to the task.
     */
    public void setDeadLine(final LocalDate deadline) {
        //The deadline was previously checked and is not null.
        this.deadline = deadline;
    }

    /**
     * Returns the task number.
     * @return the task number.
     */
    public int getTaskNumber() {
        return this.taskNumber;
    }

    /**
     * Returns the name of the task.
     * @return the name of the task.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the tags of the task.
     * @return a list of the tags of the tag.
     */
    public List<String> getTags() {
        return new LinkedList<>(this.tags);
    }

    /**
     * Adds a tag for the task, it also tags the task with the given tag.
     * The tag can come from a list.
     * @param tag the tag to be added and tagged to the task.
     * @param isListTag decides if the tag is comming from a list o if it is not.
     */
    public void addTag(final String tag, boolean isListTag) {
        if (!isListTag) {
            this.tags.add(tag);
        } else {
            //adds it to the tag list.
            this.taskListTags.add(tag);
        }
    }

    /**
     * Adds a sub task to the task.
     * @param subTask the subtask to be added.
     */
    public void addSubTask(Task subTask) {
        subTask.parentTask = this;
        subTask.subTaskNumber = this.subTasks.size() + 1;
        this.subTasks.add(subTask);
        this.subTasks.sort(Task::compareTo);
        //the sub task list will be sorted.
    }


    /**
     * Returns the subtasks of the task.
     * @return a list of the subtask of the task.
     */
    public List<Task> getSubTasks() {
        this.subTasks.sort(Task::compareTo);
        return new LinkedList<>(this.subTasks);
    }

    /**
     * Checks if the task is deleted.
     * @return true if the task is deleted, false otherwise.
     */
    public boolean isDeleted() {
        return this.deleted;
    }


    /**
     * Sets the current delete status for the task.
     * @param deletedStatus the status to be set.
     */
    public void setDeleted(boolean deletedStatus) {
        this.deleted = deletedStatus;
    }


    /**
     * Deletes a task and all the containing subtasks in a recursive form.
     * @return the number of task that had beeen deleted.
     */
    public int deleteTask() {
        int sum = 0;
        for (Task task : this.subTasks) {
            if (!task.isDeleted()) {
                //the task to be deleted must not be deleted.
                sum += task.deleteTask() + 1;
            }
        }

        this.deleted = true;
        if (this.parentTask != null) {
            this.subTaskNumber = parentTask.deletedTask + 1;
            parentTask.deletedTask++;
        }
        this.wasDeleted = true;
        return sum;
    }

    /**
     * Deletes a sub task from the task. It deletes it from the subtask list.
     * It also sets the subtask parent to null.
     * @param subTask the subtask to be deleted.
     */
    public void deleteSubTask(Task subTask) {
        subTask.parentTask = null;
        this.subTasks.remove(subTask);
    }

    /**
     * Checks if the task name contains the given string sequence.
     * Checks whether the task parent task contains the given stringn sequence.
     * @param namePart the string to be checked.
     * @return true if the name contains the string sequence, false otherwise.
     */
    public boolean containsString(final String namePart) {
        if (this.name.contains(namePart)) {
            return true;
        } else if (this.parentTask != null) {
            return this.parentTask.containsString(namePart);
        }
        return false;
    }

    /**
     * Returns the parent task of the task.
     * @return the parent of the task.
     */
    public Task getParentTask() {
        return this.parentTask;
    }

    /**
     * Marks the task as printed.
     */
    public void setPrinted() {
        this.printed = true;
    }

    /**
     * Checks if the task has been printed.
     * @return true if the task has ben printed, false otherwise.
     */
    public boolean isPrinted() {
        return this.printed;
    }

    /**
     * Marks the task and the subtasks as unprinted in a recursive form.
     */
    public void setUnprinted() {
        for (Task task : this.subTasks) {
            task.setUnprinted();
        }
        this.printed = false;
    }


    /**
     * Adjust the positon of a given task within the current taks's subtasks list.
     * If the task is found in the subtasks list, it is removed and then added again, placing it at the end of the list.
     * If the task is not found directly, the method recursively traverses through subtasks to perform the adjusment.
     * @param task
     */
    public void addjustList(Task task) {
        if (this.subTasks.contains(task)) { // if the taks is found as a direct subtask.
            this.subTasks.remove(task);
            this.subTasks.add(task);
        } else { // search the task in the subtasks from the currrent task subtasks.
            for (Task tasks : this.subTasks) {
                tasks.addjustList(task);
            }
        }
    }

    /**
     * Returns the tags from the listTask containing the task.
     * @return a list of the tags for the task marked with the ListTask.
     */
    public List<String> getTaskListTags() {
        return new LinkedList<>(this.taskListTags);
    }

    /**
     * Gives a string representation of the task object.
     * @return the string representation of the task object.
     */
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (isDone()) {
            stringBuilder.append(TASK_REPRESENTATION_FORMAT.formatted(TOGGLE_CHAR_FORMAT, this.name));
        } else {
            stringBuilder.append(TASK_REPRESENTATION_FORMAT.formatted(DELIMITER, this.name));
        }
        if (this.priority != Priority.ND) {
            stringBuilder.append(PRIORITY_REPRESENTATION_FORMAT.formatted(this.priority.getValue()));
        }
        if (getDeadline() != null || !getTags().isEmpty()) {
            stringBuilder.append(DATE_TAG_SEPARATOR);
        }
        if (!getTags().isEmpty()) {
            StringJoiner tagsString = new StringJoiner(TAG_SEPARATOR);
            for (String tag : getTags()) {
                tagsString.add(tag);
            }
            stringBuilder.append(TAG_REPRESENTATION_FORMAT.formatted(tagsString.toString()));
        }
        if (getDeadline() != null) {
            stringBuilder.append(DATE_REPRESENTATION_FORMAT.formatted(getDeadline().toString()));
        }
        return stringBuilder.toString();
    }

    /**
     * Compares two tasks.
     * @param o the object to be compared.
     * @return  a negative integer, zero o a positive integer, as this task is less than, equal to, or greater
     *     than the specified task.
     */
    public int compareTo(Task o) {
        if (wasDeleted && !o.wasDeleted) {
            return 1;
        } else if (!wasDeleted && o.wasDeleted) {
            return -1;
        } else if (wasDeleted && o.wasDeleted) {
            if (this.subTaskNumber < o.subTaskNumber) {
                return 1;
            } else if (this.subTaskNumber < o.subTaskNumber) {
                return -1;
            }
        }
        if (this.priority == o.priority && (subTaskNumber != 0 && o.subTaskNumber != 0)) {
            return Integer.compare(subTaskNumber, o.subTaskNumber);
        } else if (this.priority == o.priority) {
            return Integer.compare(this.getTaskNumber(), o.getTaskNumber());
        } else if ((Integer.compare(o.priority.getPriorityOrder(), this.priority.getPriorityOrder()) != 0)) {
            return Integer.compare(o.priority.getPriorityOrder(), this.priority.getPriorityOrder());
        } else {
            return Integer.compare(this.taskNumber, o.taskNumber);
        }
    }
}
