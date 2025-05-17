package ru.job4j.bmb.model;

import jakarta.persistence.*;
import ru.job4j.bmb.constants.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "mb_setting")
public class Setting {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private String text;
		private String command;
		private String desc;
		@ManyToOne()
		@JoinColumn(name = "parent_id")
		private Setting parent;
		@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
		private List<Setting> children = new ArrayList<>();

		public Setting(Settings command, String desc) {
				this(command);
				this.desc = desc;
		}

		public Setting(Settings command) {
				this.command = command.getName();
				this.text = command.getValue();
		}

		public Setting() {
		}

		@Override
		public boolean equals(Object o) {
				if (this == o) {
						return true;
				}
				if (o == null || getClass() != o.getClass()) {
						return false;
				}
				Setting setting = (Setting) o;
				return Objects.equals(id, setting.id) && Objects.equals(command, setting.command);
		}

		@Override
		public int hashCode() {
				return Objects.hash(id, command);
		}

		public List<Setting> getChildren() {
				return children;
		}

		public String getDesc() {
				return desc;
		}

		public void setDesc(String desc) {
				this.desc = desc;
		}

		public String getCommand() {
				return command;
		}

		public String getText() {
				return text;
		}

		public void setText(String text) {
				this.text = text;
		}

		public Long getId() {
				return id;
		}

		public void setId(Long id) {
				this.id = id;
		}

		public Setting addChild(Setting child) {
				children.add(child);
				child.setParent(this);
				return this;
		}

		public void removeChild(Setting child) {
				children.remove(child);
				child.setParent(null);
		}

		public Setting getParent() {
				return parent;
		}

		public void setParent(Setting parent) {
				this.parent = parent;
		}

		public Setting getRootParent() {
				if (this.parent == null) {
						return this;
				} else {
						return this.parent.getRootParent();
				}
		}

		@Override
		public String toString() {
				return command;
		}
}
