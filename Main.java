/**
 * Zadanie rekrutacyjne Horus
 *
 * @author Mateusz Skorupiński
 */

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    interface Structure {
        // zwraca dowolny element o podanym kolorze
        Optional<Block> findBlockByColor(String color);

        // zwraca wszystkie elementy z danego materiału
        List<Block> findBlocksByMaterial(String material);

        //zwraca liczbę wszystkich elementów tworzących strukturę
        int count();
    }

    public class Wall implements Structure {
        private List<Block> blocks;

        /**
         * Redukuje listę bloków do bloków prostych (nie skladających się z innych bloków)
         * @return jednowymiarowa lista prostych bloków (nie CompositeBlock)
         */
        private List<Block> getFlatBlockList() {
            List<Block> inner_blocks = blocks.stream()
                    .filter(b -> b instanceof CompositeBlock)
                    .map(b -> (CompositeBlock) b)
                    .map(CompositeBlock::getBlocks)
                    .flatMap(List::stream)
                    .toList();

            List<Block> simple_blocks = blocks.stream()
                    .filter(b -> !(b instanceof CompositeBlock))
                    .toList();

            List<Block> all_blocks = Stream.of(inner_blocks, simple_blocks)
                    .flatMap(List::stream)
                    .toList();

            return all_blocks;
        }

        /**
         * Szuka dowolnego bloku prostego o podanym w argumencie kolorze
         * @param color - kolor bloku
         * @return obiekt klasy Optional, który zawiera obiekt klasy Block
         * w zależności od tego, czy ten został odnaleziony w liście
         */
        @Override
        public Optional<Block> findBlockByColor(String color) {
            return getFlatBlockList().stream()
                    .filter(b -> b.getColor().equals(color))
                    .findAny();
        }

        /**
         * Zwraca listę bloków zbudowanych z podanego w argumencie materiału
         * @param material - nazwa materiału
         * @return jednowymiarowa lista prostych bloków spełniających warunek
         */
        @Override
        public List<Block> findBlocksByMaterial(String material) {
            return getFlatBlockList().stream()
                    .filter(b -> b.getMaterial().equals(material))
                    .collect(Collectors.toList());
        }

        /**
         * @return wielkość listy prostych bloków z których składa się struktura
         */
        @Override
        public int count() {
            return getFlatBlockList().size();
        }
    }

    interface Block {
        String getColor();
        String getMaterial();
    }

    interface CompositeBlock extends Block {
        List<Block> getBlocks();
    }
}
