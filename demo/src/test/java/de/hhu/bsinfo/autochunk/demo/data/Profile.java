package de.hhu.bsinfo.autochunk.demo.data;

import java.util.Arrays;

import de.hhu.bsinfo.autochunk.demo.util.RandomUtil;

public class Profile {

    private final long m_id;

    private final String m_firstname;

    private final String m_lastname;

    private final long[] m_friends;

    private final Storyboard m_storyboard;

    public Profile(long p_id, String p_firstname, String p_lastname, long[] p_friends,
            Storyboard p_storyboard) {
        m_id = p_id;
        m_firstname = p_firstname;
        m_lastname = p_lastname;
        m_friends = p_friends;
        m_storyboard = p_storyboard;
    }

    public long getId() {
        return m_id;
    }

    public long[] getFriends() {
        return m_friends;
    }

    public Storyboard getStoryboard() {
        return m_storyboard;
    }

    public String getFirstname() {
        return m_firstname;
    }

    public String getLastname() {
        return m_lastname;
    }

    public static Profile random() {
        return new Profile(RandomUtil.randomLong(), "John", "Doe", RandomUtil.randomLongArray(128), Storyboard.random());
    }

    @Override
    public boolean equals(Object p_o) {
        if (this == p_o) {
            return true;
        }
        if (p_o == null || getClass() != p_o.getClass()) {
            return false;
        }
        Profile profile = (Profile) p_o;
        return m_id == profile.m_id &&
                m_firstname.equals(profile.m_firstname) &&
                m_lastname.equals(profile.m_lastname) &&
                Arrays.equals(m_friends, profile.m_friends) &&
                m_storyboard.equals(profile.m_storyboard);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(m_id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
