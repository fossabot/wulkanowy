package io.github.wulkanowy.api.notes;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.wulkanowy.api.SnP;
import io.github.wulkanowy.api.VulcanException;

import static io.github.wulkanowy.api.DateTimeUtilsKt.getFormattedDate;

public class NotesList {

    private static final String NOTES_PAGE_URL = "UwagiOsiagniecia.mvc/Wszystkie";

    private SnP snp;

    public NotesList(SnP snp) {
        this.snp = snp;
    }

    public List<Note> getAllNotes() throws IOException, VulcanException {
        Element pageFragment = snp.getSnPPageDocument(NOTES_PAGE_URL)
                .select(".mainContainer > div").get(0);
        Elements items = pageFragment.select("article");
        Elements dates = pageFragment.select("h2");

        List<Note> notes = new ArrayList<>();

        int index = 0;
        for (Element item : items) {
            notes.add(new Note()
                    .setDate(getFormattedDate(dates.get(index++).text()))
                    .setTeacher(snp.getRowDataChildValue(item, 1))
                    .setCategory(snp.getRowDataChildValue(item, 2))
                    .setContent(snp.getRowDataChildValue(item, 3))
            );
        }

        return notes;
    }
}
