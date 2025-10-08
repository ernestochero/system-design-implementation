package mini_project_01.repository

import cats.effect.IO
import cats.effect.kernel.Ref
import mini_project_01.domain.Asset

class AssetRepository(ref: Ref[IO, Map[String, Asset]]) {
  def upsert(asset: Asset): IO[Unit] =
    ref.update(_.updated(asset.id, asset))

  def getAll: IO[List[Asset]] =
    ref.get.map(_.values.toList)

  def find(id: String): IO[Option[Asset]] =
    ref.get.map(_.get(id))
}

object AssetRepository {
  def create: IO[AssetRepository] =
    Ref.of[IO, Map[String, Asset]](Map.empty).map(new AssetRepository(_))
}
