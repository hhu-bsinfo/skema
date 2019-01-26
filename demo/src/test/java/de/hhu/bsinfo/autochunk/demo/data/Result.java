package de.hhu.bsinfo.autochunk.demo.data;

import java.util.Objects;

public class Result {

    private final Status m_status;

    public Result() {
        this(null);
    }

    public Result(Status m_status) {
        this.m_status = m_status;
    }

    public Status getStatus() {
        return m_status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return m_status == result.m_status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(m_status);
    }
}
