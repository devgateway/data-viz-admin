package org.devgateway.toolkit.persistence.dao.data;

import org.devgateway.toolkit.persistence.dao.FileMetadata;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CSVDataset extends Dataset {

    @Audited
    private String description;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(cascade = CascadeType.ALL)
    @Audited
    private Set<FileMetadata> files;

    public Set<FileMetadata> getFiles() {
        return files;
    }

    public void setFiles(final Set<FileMetadata> files) {
        this.files = files;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
